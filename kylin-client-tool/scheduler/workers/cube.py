# -*- coding: utf-8 -*-
__author__ = 'Huang, Hua'

import time
import datetime
import calendar
from dateutil import parser
from jobs.build import CubeBuildJob
from jobs.cube import CubeJob
from models.request import JobBuildRequest
from models.job import JobInstance, CubeJobStatus
from settings.settings import KYLIN_JOB_MAX_COCURRENT, KYLIN_JOB_MAX_RETRY

class CubeWorkerStatus:
    ERROR = 'ERROR'
    SUCCESS = 'SUCCESS'
    WORKING = 'WORKING'

class CubeWorker:
    job_instance_dict = {}
    job_retry_dict = {}
    scheduler = None
    run_cube_job_id = None
    check_cube_job_id = None

    def __init__(self):
        pass

    @staticmethod
    def run_cube_job(build_type, start_time, end_time):
        if CubeWorker.all_finished():
            return True

        running_job_list = CubeWorker.get_current_running_job()
        print "current running %d jobs" % len(running_job_list)

        if running_job_list and len(running_job_list) >= KYLIN_JOB_MAX_COCURRENT:
            print "will not schedule new jobs this time because running job number >= the max cocurrent job number %d" % KYLIN_JOB_MAX_COCURRENT
        else:
            max_allow = KYLIN_JOB_MAX_COCURRENT - len(running_job_list)
            for cube_name in CubeWorker.job_instance_dict:
                if max_allow <= 0: break

                job_instance = CubeWorker.job_instance_dict[cube_name]
                if job_instance is None or (
                    isinstance(job_instance, JobInstance) and job_instance.get_status() == CubeJobStatus.ERROR):
                    try_cnt = CubeWorker.job_retry_dict.get(cube_name, -1)

                    if try_cnt >= KYLIN_JOB_MAX_RETRY:
                        # have already tried KYLIN_JOB_MAX_RETRY times
                        print "Reached KYLIN_JOB_MAX_RETRY for %s" % cube_name
                        CubeWorker.job_instance_dict[cube_name] = 0
                    else:
                        # try to cancel the error cube build segment
                        error_job_list = CubeJob.get_cube_job(cube_name, CubeJob.ERROR_JOB_STATUS)
                        if error_job_list:
                            for error_job in error_job_list:
                                CubeBuildJob.cancel_job(error_job.uuid)
                                print "cancel an error job with uuid= %s for cube= %s" % (error_job.uuid, cube_name)

                        # run cube job
                        instance_list = None
                        build_request = JobBuildRequest()
                        if build_type is not None:
                            build_request.buildType = build_type
                            
                        if build_type == JobBuildRequest.REFRESH:
                            instance_list = CubeJob.list_cubes(cube_name)
                            
                            if(len(instance_list) == 0):
                                raise Exception("Cube does not have segments.")
                            
                            if start_time is None or end_time is None:
                                segments = [instance_list[0].segments[len(instance_list[0].segments) - 1]]
                            else:
                                segments = CubeWorker.determine_segment_range(instance_list[0].segments, start_time, end_time)
                            
                            if(len(segments) < 1):
                                raise LookupError("Entered date: %s and %s is out of range of segments" % (str(start_time), str(end_time)))
                            
                            build_request.startTime = int(segments[0].date_range_start)
                            build_request.endTime = int(segments[len(segments) - 1].date_range_end)
                            number_of_segments = len(segments)
                            print "merging %d segments (%s) from: %s to: %s" % (number_of_segments, ', '.join([s.uuid for s in segments]), build_request.startTime, build_request.endTime)
                        elif build_type == JobBuildRequest.BUILD:
                            if end_time is not None:
                                # build_request.startTime = instance_list[0].segments[instance_list[0].segments.__len__() - 1].date_range_end
                                build_request.endTime = \
                                    (int(time.mktime(parser.parse(end_time).timetuple())) - time.timezone) * 1000
                            else:
                                d = datetime.datetime.utcnow()
                                build_request.endTime = calendar.timegm(d.utctimetuple()) * 1000

                        current_job_instance = CubeBuildJob.rebuild_cube(cube_name, build_request)

                        if current_job_instance:
                            print "schedule a cube build job for cube = %s" % cube_name
                            CubeWorker.job_instance_dict[cube_name] = current_job_instance
                            max_allow -= 1
                        CubeWorker.job_retry_dict[cube_name] = try_cnt + 1
                        
    @staticmethod
    def determine_segment_range(segments, dt_start, dt_end):
        pointer_dt_start = (int(time.mktime(parser.parse(dt_start).timetuple())) - time.timezone) * 1000
        pointer_dt_end = (int(time.mktime(parser.parse(dt_end).timetuple())) - time.timezone) * 1000
        
        if(pointer_dt_start > pointer_dt_end):
            raise Exception("Start date (%s) is older than end date (%s)!" % (str(pointer_dt_start), str(pointer_dt_end)))
        
        segments_to_refresh = []
        for segment in segments:
            if((pointer_dt_start <= segment.date_range_start and segment.date_range_end <= pointer_dt_end) or       # |..| \      
                (segment.date_range_start <= pointer_dt_start and pointer_dt_start <= segment.date_range_end) or    # .|.| \  
                (pointer_dt_end <= segment.date_range_start and segment.date_range_end <= pointer_dt_end) ):        # |.|.
                segments_to_refresh.append(segment)
         
        return sorted(segments_to_refresh, key = lambda x: x.date_range_start)
                  
          
    @staticmethod
    def check_cube_job():
        for cube_name in CubeWorker.job_instance_dict:
            job_instance = CubeWorker.job_instance_dict[cube_name]
            if isinstance(job_instance,
                          JobInstance) and job_instance.uuid and job_instance.get_status() == CubeJobStatus.RUNNING:
                current_job_instance = CubeBuildJob.get_job(job_instance.uuid)
                if current_job_instance:
                    CubeWorker.job_instance_dict[cube_name] = current_job_instance

            job_instance = CubeWorker.job_instance_dict[cube_name]
            if job_instance is None:
                print "status of cube = %s is NOT STARTED YET" % cube_name
            elif isinstance(job_instance, JobInstance):
                print "status of cube = %s is %s at %d/%d" % (cube_name, job_instance.get_status(), job_instance.get_current_step(), len(job_instance.steps))

    @staticmethod
    def get_current_running_job():
        if not CubeWorker.job_instance_dict:
            return None

        running_job_list = []
        for cube_name in CubeWorker.job_instance_dict:
            job_instance = CubeWorker.job_instance_dict[cube_name]

            if job_instance and isinstance(job_instance,
                                           JobInstance) and job_instance.get_status() == CubeJobStatus.RUNNING:
                running_job_list.append(job_instance)

        return running_job_list

    @staticmethod
    def all_finished():
        if not CubeWorker.job_instance_dict:
            return True

        for cube_name in CubeWorker.job_instance_dict:
            job_instance = CubeWorker.job_instance_dict[cube_name]

            if job_instance == 0:
                pass
            elif job_instance is None:
                return False
            elif isinstance(job_instance, JobInstance) and job_instance.get_status() == CubeJobStatus.RUNNING:
                return False

        return True
        
    @staticmethod
    def get_status():
        if  CubeWorker.all_finished() == False:
            return CubeWorkerStatus.WORKING

        for cube_name in CubeWorker.job_instance_dict:
            job_instance = CubeWorker.job_instance_dict[cube_name]

            if job_instance == 0:
                return CubeWorkerStatus.ERROR
            elif job_instance is None:
                return CubeWorkerStatus.ERROR
            elif isinstance(job_instance, JobInstance) and job_instance.get_status() == CubeJobStatus.ERROR:
                return CubeWorkerStatus.ERROR

        return CubeWorkerStatus.SUCCESS
