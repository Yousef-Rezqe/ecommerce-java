<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<c:set var="pageTitle" value="Sign In" scope="request"/>
<%@ include file="_layout-header.jspf" %>
<div class="auth-shell">
  <section class="auth-card">
    <div class="auth-brand"><span style="font-size:1.3rem;">📖</span> CodeShelf</div>
    <h1>Welcome back</h1>
    <p class="sub">Sign in to your account to continue.</p>
    <c:if test="${not empty error}"><div class="banner-error"><c:out value="${error}"/></div></c:if>
    <form method="post" action="${pageContext.request.contextPath}/login">
      <div class="field"><label>Email</label><input type="email" name="email" required autocomplete="email" placeholder="you@example.com"/></div>
      <div class="field"><label>Password</label><input type="password" name="password" required autocomplete="current-password" placeholder="Your password"/></div>
      <button type="submit" class="btn btn-primary btn-block btn-lg">Sign In</button>
    </form>
    <p class="alt">New here? <a href="${pageContext.request.contextPath}/signup">Create an account</a></p>
  </section>
</div>
<%@ include file="_layout-footer.jspf" %>
