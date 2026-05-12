<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<%@ taglib prefix="fmt" uri="jakarta.tags.fmt" %>
<c:set var="pageTitle" value="Order Confirmed" scope="request"/>
<%@ include file="_layout-header.jspf" %>
<div class="container" style="max-width:640px;margin:3rem auto;">
  <div class="dash-card" style="text-align:center;padding:2.5rem;">
    <div style="font-size:4rem;margin-bottom:1rem;">🎉</div>
    <h1 style="color:var(--brand);margin-bottom:.5rem;">Order Confirmed!</h1>
    <p style="color:var(--muted);margin-bottom:2rem;">
      Thank you for your order. We'll ship your book(s) to the address you provided.
    </p>
    <c:if test="${not empty order}">
      <div style="background:var(--bg-soft);border:1px solid var(--line);border-radius:var(--radius-lg);
                  padding:1.5rem;text-align:left;margin-bottom:1.5rem;">
        <div style="display:flex;justify-content:space-between;align-items:center;margin-bottom:1rem;
                    padding-bottom:.75rem;border-bottom:1px solid var(--line);">
          <span style="font-weight:700;color:var(--brand);">Order #${order.id}</span>
          <span style="background:#eff6ff;color:#1e40af;padding:3px 10px;border-radius:999px;
                       font-size:.75rem;font-weight:600;border:1px solid #bfdbfe;">
            📦 Processing
          </span>
        </div>
        <div style="display:grid;gap:.6rem;font-size:.88rem;">
          <div style="display:flex;justify-content:space-between;">
            <span style="color:var(--muted);">Book</span>
            <span style="font-weight:600;"><c:out value="${order.productName}"/></span>
          </div>
          <div style="display:flex;justify-content:space-between;">
            <span style="color:var(--muted);">Quantity</span>
            <span>${order.quantity}</span>
          </div>
          <div style="display:flex;justify-content:space-between;">
            <span style="color:var(--muted);">Unit Price</span>
            <span>$<fmt:formatNumber value="${order.unitPrice}" pattern="#,##0.00"/></span>
          </div>
          <div style="display:flex;justify-content:space-between;padding-top:.5rem;
                      border-top:1px solid var(--line);font-weight:700;font-size:1rem;">
            <span>Total</span>
            <span style="color:var(--brand);">$<fmt:formatNumber value="${order.totalPrice}" pattern="#,##0.00"/></span>
          </div>
        </div>
        <div style="margin-top:1rem;padding-top:.75rem;border-top:1px solid var(--line);
                    display:grid;gap:.4rem;font-size:.85rem;">
          <div><span style="color:var(--muted);">Deliver to: </span><strong><c:out value="${order.fullName}"/></strong></div>
          <div><span style="color:var(--muted);">Address: </span><c:out value="${order.address}"/>, <c:out value="${order.city}"/></div>
          <div><span style="color:var(--muted);">Phone: </span><c:out value="${order.phone}"/></div>
          <c:if test="${not empty order.notes}">
            <div><span style="color:var(--muted);">Notes: </span><c:out value="${order.notes}"/></div>
          </c:if>
          <div><span style="color:var(--muted);">Placed: </span><c:out value="${order.createdAtFormatted}"/></div>
        </div>
      </div>
    </c:if>
    <div style="display:flex;gap:.75rem;justify-content:center;flex-wrap:wrap;">
      <a href="${pageContext.request.contextPath}/account" class="btn btn-primary">View My Orders</a>
      <a href="${pageContext.request.contextPath}/shop" class="btn btn-ghost">Continue Shopping</a>
    </div>
  </div>
</div>
<%@ include file="_layout-footer.jspf" %>
