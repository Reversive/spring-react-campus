<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://www.springframework.org/tags" prefix="spring" %>
<!DOCTYPE html>
<html lang="es">
<head>
  <title><spring:message code="400.page.title"/></title>
  <c:import url="config/general-head.jsp"/>

</head>
<body>
<div class="page-organizer">
  <%@ include file="components/navbar.jsp" %>
  <div class="page-container">
    <h1 class="error-title"><spring:message code="400.page.message"/></h1>
    <p>
      <a href="<c:url value="/portal"/>"
         class="styleless-anchor, back-to-portal-button">
        <spring:message code="back.to.portal.button" htmlEscape="true"/>
      </a>
    </p>
  </div>
  <jsp:include page="components/footer.jsp"/>
</div>
</body>
</html>