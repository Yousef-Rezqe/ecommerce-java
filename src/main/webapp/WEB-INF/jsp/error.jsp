<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<c:set var="pageTitle" value="Error" scope="request"/>
<%@ include file="_layout-header.jspf" %>
<div class="container">
  <section class="error-card">
    <c:choose>
      <c:when test="${statusCode == 401}"><div style="font-size:2.5rem;">🔒</div><h1>Sign In Required</h1><p class="muted">Please sign in to access this page.</p><div style="margin-top:1.25rem;display:flex;gap:.65rem;justify-content:center;"><a href="${pageContext.request.contextPath}/login" class="btn btn-primary">Sign In</a><a href="${pageContext.request.contextPath}/signup" class="btn btn-ghost">Register</a></div></c:when>
      <c:when test="${statusCode == 403}"><div style="font-size:2.5rem;">��</div><h1>Access Denied</h1><p class="muted"><c:choose><c:when test="${not empty errorMessage}"><c:out value="${errorMessage}"/></c:when><c:otherwise>You don't have permission to access this page.</c:otherwise></c:choose></p><div style="margin-top:1.25rem;"><a href="${pageContext.request.contextPath}/home" class="btn btn-primary">Go Home</a></div></c:when>
      <c:when test="${statusCode == 404}"><div style="font-size:2.5rem;">🔍</div><h1>Page Not Found</h1><p class="muted">The page you're looking for doesn't exist.</p><div style="margin-top:1.25rem;display:flex;gap:.65rem;justify-content:center;"><a href="${pageContext.request.contextPath}/home" class="btn btn-primary">Go Home</a><a href="${pageContext.request.contextPath}/shop" class="btn btn-ghost">Browse Books</a></div></c:when>
      <c:when test="${statusCode == 429}"><div style="font-size:2.5rem;">⏳</div><h1>Too Many Requests</h1><p class="muted">Please wait a moment and try again.</p><div style="margin-top:1.25rem;"><a href="${pageContext.request.contextPath}/home" class="btn btn-primary">Go Home</a></div></c:when>
      <c:otherwise>
        <div style="font-size:2.5rem;">⚠️</div><h1>Something Went Wrong</h1>
        <p class="muted">Status <strong><c:out value="${statusCode}"/></strong><c:if test="${not empty failedUri}"> &middot; <code><c:out value="${failedUri}"/></code></c:if></p>
        <c:if test="${not empty errorMessage}"><p class="muted"><c:out value="${errorMessage}"/></p></c:if>
        <div style="margin-top:1.25rem;display:flex;gap:.65rem;justify-content:center;"><a href="${pageContext.request.contextPath}/home" class="btn btn-primary">Go Home</a><a href="javascript:history.back()" class="btn btn-ghost">Go Back</a></div>
      </c:otherwise>
    </c:choose>
  </section>
</div>
<%@ include file="_layout-footer.jspf" %>
