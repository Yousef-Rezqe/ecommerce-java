<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<%@ taglib prefix="fmt" uri="jakarta.tags.fmt" %>
<c:set var="pageTitle" value="Place Order" scope="request"/>
<%@ include file="_layout-header.jspf" %>
<div class="page-hero">
  <div class="container"><h1>📦 Place Your Order</h1><p>Fill in your delivery details to complete your purchase</p></div>
</div>
<div class="container" style="max-width:900px;margin-bottom:3rem;">
  <c:if test="${not empty error}"><div class="banner-error" style="margin-bottom:1rem;">⚠️ <c:out value="${error}"/></div></c:if>
  <div style="display:grid;grid-template-columns:1fr 340px;gap:1.5rem;align-items:start;">
    <div class="dash-card">
      <h2 style="margin-bottom:1.25rem;font-size:1.1rem;color:var(--brand);">🏠 Delivery Details</h2>
      <form method="post" action="${pageContext.request.contextPath}/order">
        <input type="hidden" name="productId" value="${product.id}"/>
        <input type="hidden" name="quantity" value="${qty}"/>
        <div class="field"><label>Full Name *</label><input name="fullName" required placeholder="John Doe" maxlength="128"/></div>
        <div class="field-row">
          <div class="field"><label>Phone Number *</label><input name="phone" required placeholder="+1 555 000 0000" maxlength="32"/></div>
          <div class="field"><label>City *</label><input name="city" required placeholder="New York" maxlength="100"/></div>
        </div>
        <div class="field"><label>Delivery Address *</label><textarea name="address" required rows="2" maxlength="500" placeholder="Street, building, apartment..."></textarea></div>
        <div class="field"><label>Order Notes (optional)</label><textarea name="notes" rows="2" maxlength="500" placeholder="Any special instructions..."></textarea></div>
        <button type="submit" class="btn btn-primary btn-block btn-lg" <c:if test="${product.stock == 0}">disabled</c:if>>✅ Confirm Order</button>
        <a href="${pageContext.request.contextPath}/products/${product.id}" class="btn btn-ghost btn-block" style="margin-top:.5rem;">← Back to Book</a>
      </form>
    </div>
    <div>
      <div class="dash-card" style="position:sticky;top:80px;">
        <h2 style="margin-bottom:1rem;font-size:1rem;color:var(--brand);">🧾 Order Summary</h2>
        <div style="display:flex;gap:.75rem;align-items:flex-start;margin-bottom:1rem;padding-bottom:1rem;border-bottom:1px solid var(--line);">
          <div style="width:50px;height:50px;border-radius:var(--radius);background:linear-gradient(135deg,#eff6ff,#dbeafe);display:grid;place-items:center;font-size:1.5rem;flex-shrink:0;">📖</div>
          <div>
            <div style="font-weight:600;font-size:.9rem;"><c:out value="${product.name}"/></div>
            <div style="color:var(--muted);font-size:.78rem;margin-top:.15rem;">Qty: ${qty}</div>
          </div>
        </div>
        <div style="display:grid;gap:.45rem;font-size:.88rem;">
          <div style="display:flex;justify-content:space-between;"><span style="color:var(--muted);">Unit price</span><span>$<fmt:formatNumber value="${product.price}" pattern="#,##0.00"/></span></div>
          <div style="display:flex;justify-content:space-between;"><span style="color:var(--muted);">Quantity</span><span>${qty}</span></div>
          <div style="display:flex;justify-content:space-between;"><span style="color:var(--muted);">Shipping</span><span style="color:var(--success);">Free</span></div>
          <div style="display:flex;justify-content:space-between;padding-top:.5rem;border-top:1px solid var(--line);font-weight:700;font-size:1rem;">
            <span>Total</span>
            <span style="color:var(--brand);">$<fmt:formatNumber value="${product.price * qty}" pattern="#,##0.00"/></span>
          </div>
        </div>
        <div style="margin-top:1rem;padding:.65rem;background:#eff6ff;border:1px solid #bfdbfe;border-radius:var(--radius);font-size:.78rem;color:#1e40af;">
          📦 Your order will be shipped to the address you provide above.
        </div>
      </div>
    </div>
  </div>
</div>
<%@ include file="_layout-footer.jspf" %>
