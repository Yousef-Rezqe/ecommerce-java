<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<c:set var="pageTitle" value="Create Account" scope="request"/>
<%@ include file="_layout-header.jspf" %>
<div class="auth-shell">
  <section class="auth-card">
    <div class="auth-brand"><span style="font-size:1.3rem;">📖</span> CodeShelf</div>
    <h1>Create your account</h1>
    <p class="sub">Join thousands of developers who read with CodeShelf.</p>
    <c:if test="${not empty error}"><div class="banner-error"><c:out value="${error}"/></div></c:if>
    <form method="post" action="${pageContext.request.contextPath}/signup">
      <div class="field"><label>Username</label><input name="username" required minlength="3" maxlength="64" autocomplete="username" placeholder="johndoe"/></div>
      <div class="field"><label>Email</label><input type="email" name="email" required autocomplete="email" placeholder="you@example.com"/></div>
      <div class="field"><label>Password</label><input type="password" name="password" required minlength="6" autocomplete="new-password" placeholder="At least 6 characters"/></div>
      <button type="submit" class="btn btn-primary btn-block btn-lg">Create Account</button>
    </form>
    <p class="alt">Already have an account? <a href="${pageContext.request.contextPath}/login">Sign in</a></p>
  </section>
</div>
<%@ include file="_layout-footer.jspf" %>
