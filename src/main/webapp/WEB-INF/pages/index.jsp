<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://www.springframework.org/tags/form" prefix="form"%>
<link rel="stylesheet" href="http://maxcdn.bootstrapcdn.com/bootstrap/3.3.4/css/bootstrap.min.css">
<html>
<head>
	<script>
		setTimeout('window.location.reload();', 20000);
	</script>
</head>
<body>
<h1>Job Schedular Demo using Spring and Quartz</h1>
 	
 <h3>Below is schedule of Cron Jobs </h3> 
 <div class="btn-group">
 	<c:url value="/pauseAll" var="pauseAll"></c:url>
 	<c:url value="/resumeAll" var="resumeAll"></c:url>
 	<a href='<c:out value="${pauseAll }"></c:out>' class="btn btn-info"><span class="glyphicon glyphicon-pause"></span>  Pause All Jobs</a>
 	<a href='<c:out value="${resumeAll }"></c:out>' class="btn btn-success"><span class="glyphicon glyphicon-play"></span> Resume All Jobs</a>
 </div>
<c:if test="${not empty jobsInfo }">
 	<table class="table table-striped table-bordered">
 		<thead>
 			<tr>
 				<th>
 					JOB NAME
 				</th>
 				<th>
 					JOB GROUP
 				</th>
 				<th>
 					JOB PREVIOUS FIRE TIME
 				</th>
 				<th>
 					JOB NEXT FIRE TIME
 				</th>
 				<th>
 					JOB STATUS
 				</th>
 				<th>
 					MANUAL START
 				</th>
 				<th>
 					PAUSE or RESUME JOB
 				</th>
 			</tr>
 		</thead>
 		<c:forEach items="${ jobsInfo}" var="jobInfo" varStatus="status">
	 		<tbody>
	 			<tr>
	 				<td>
	 					<c:out value="${jobInfo.jobName }"></c:out>
	 				</td>
	 				<td>
	 					<c:out value="${jobInfo.jobGroup }"></c:out>
	 				</td>
	 				<td>
	 					<c:out value="${jobInfo.prevFireTime }"></c:out>
	 				</td>
	 				<td>
	 					<c:out value="${jobInfo.nextFireTime }"></c:out>
	 				</td>
	 				<td>
	 					<c:out value="${jobInfo.jobStatus }"></c:out>
	 				</td>
	 				<td>
	 					<form:form role="form" method="POST" action="startScheduledJobNow">
							<input type="hidden" name="jobName" value="${jobInfo.jobName }">
							<input type="hidden" name="jobGroup" value="${jobInfo.jobGroup }">
							<button type="submit" class="btn btn-success"><span class="glyphicon glyphicon-right"></span> Fire Now</button>	
						</form:form> 
	 				</td>
	 				<td>
	 					<c:choose>
	 						<c:when test="${jobInfo.jobStatus ne 'PAUSED' }">
		 						<form:form role="form" method="POST" action="pauseJob">
									<input type="hidden" name="jobName" value="${jobInfo.jobName }">
									<input type="hidden" name="jobGroup" value="${jobInfo.jobGroup }">
									<button type="submit" class="btn btn-info"><span class="glyphicon glyphicon-pause"></span> Pause Job</button>	
								</form:form>
	 						</c:when>
	 						<c:otherwise>
	 							<form:form role="form" method="POST" action="resumeJob">
									<input type="hidden" name="jobName" value="${jobInfo.jobName }">
									<input type="hidden" name="jobGroup" value="${jobInfo.jobGroup }">
									<button type="submit" class="btn btn-info"><span class="glyphicon glyphicon-play"></span> Resume Job</button>	
								</form:form>
	 						</c:otherwise>
	 					</c:choose>
	 					 
	 				</td>
	 			</tr>
	 		</tbody>
 		</c:forEach>
 	</table>
 	
</c:if>

<h3>Jobs Running...,</h3>
<c:choose>
	<c:when test="${not empty currentExecutingJobs }">
		<table class="table table-striped table-bordered">
	 		<thead>
	 			<tr>
	 				<th>
	 					JOB INSTANCE ID
	 				</th>
	 				<th>
	 					JOB NAME
	 				</th>
	 				<th>
	 					JOB GROUP
	 				</th>
	 				<th>
	 					JOB FIRE TIME
	 				</th>
	 				<th>
	 					STOP JOB
	 				</th>
	 			</tr>
	 		</thead>
	 		<c:forEach items="${ currentExecutingJobs}" var="currentJob" varStatus="status">
		 		<tbody>
		 			<tr>
		 				<td>
		 					<c:out value="${currentJob.jobInstanceIdentifier }"></c:out>
		 				</td>
		 				<td>
		 					<c:out value="${currentJob.jobName }"></c:out>
		 				</td>
		 				<td>
		 					<c:out value="${currentJob.jobGroup }"></c:out>
		 				</td>
		 				<td>
		 					<c:out value="${currentJob.fireTime }"></c:out>
		 				</td>
		 				<td>
		 					<form:form role="form" method="POST" action="stopScheduledJobNow">
								<input type="hidden" name="jobContextHashCode" value="${currentJob.jobContextHashCode }">
								<button type="submit" class="btn btn-danger"> Stop Now</button>	
							</form:form> 
		 				</td>
		 			</tr>
		 		</tbody>
	 		</c:forEach>
	 	</table>
	</c:when>
	<c:otherwise>
		<h4>No jobs are running at this time.</h4>
	</c:otherwise>
</c:choose>
</body>
</html>