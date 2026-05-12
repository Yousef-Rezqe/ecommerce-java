<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<%@ taglib prefix="fmt" uri="jakarta.tags.fmt" %>
<c:set var="pageTitle" value="Edit Book" scope="request"/>
<%@ include file="_layout-header.jspf" %>
<div class="container">
  <nav class="breadcrumb">
    <a href="${pageContext.request.contextPath}/admin">Admin</a><span class="sep">/</span>
    <a href="${pageContext.request.contextPath}/products/${product.id}"><c:out value="${product.name}"/></a><span class="sep">/</span>
    <span>Edit</span>
  </nav>
  <div class="auth-card" style="max-width:680px;margin:0 auto;">
    <h1 style="font-size:1.4rem;margin-bottom:.25rem;">📝 Edit Book</h1>
    <p class="muted" style="margin-bottom:1.25rem;">Book #${product.id}</p>
    <form method="post" action="${pageContext.request.contextPath}/products/${product.id}/edit">
      <div class="field-row">
        <div class="field"><label>Title *</label><input name="name" required maxlength="150" value="<c:out value='${product.name}'/>"/></div>
        <div class="field"><label>Cover Image URL</label><input name="imageUrl" type="url" maxlength="500" value="<c:out value='${product.imageUrl}'/>"/></div>
      </div>
      <div class="field-row">
        <div class="field"><label>Price (USD) *</label><input name="price" type="number" step="0.01" min="0" required value="<fmt:formatNumber value='${product.price}' pattern='0.00'/>"/></div>
        <div class="field"><label>Stock *</label><input name="stock" type="number" min="0" required value="${product.stock}"/></div>
      </div>
      <div class="field"><label>Description / About this book</label><textarea name="description" rows="4" maxlength="2000"><c:out value="${product.description}"/></textarea></div>
      <div style="display:flex;gap:.65rem;flex-wrap:wrap;">
        <button type="submit" class="btn btn-primary">Save Changes</button>
        <a href="${pageContext.request.contextPath}/products/${product.id}" class="btn btn-ghost">Cancel</a>
      </div>
    </form>
    <form method="post" action="${pageContext.request.contextPath}/products/${product.id}/delete"
          onsubmit="return confirm('Permanently delete this book?');"
          style="margin-top:1.25rem;padding-top:1rem;border-top:1px solid var(--line);">
      <button type="submit" class="btn btn-danger">Delete Book</button>
    </form>
  </div>
</div>
<%@ include file="_layout-footer.jspf" %>
