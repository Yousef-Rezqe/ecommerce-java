<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<%@ taglib prefix="fmt" uri="jakarta.tags.fmt" %>
<%@ taglib prefix="fn" uri="jakarta.tags.functions" %>
<c:set var="pageTitle" value="My Dashboard" scope="request"/>
<%@ include file="_layout-header.jspf" %>
<style>
  .tab-panel { display:none; }
  .tab-panel.active { display:grid; gap:1.25rem; }
  .dash-nav a.tab-active { background:#eff6ff; color:var(--brand); }
  .order-status-pending   { background:#fffbeb;color:#92400e;border:1px solid #fde68a; }
  .order-status-confirmed { background:#ecfdf5;color:#065f46;border:1px solid #a7f3d0; }
  .order-status-delivered { background:#eff6ff;color:#1e40af;border:1px solid #bfdbfe; }
  .order-status-cancelled { background:#fef2f2;color:#991b1b;border:1px solid #fecaca; }
  .order-badge { padding:3px 10px;border-radius:999px;font-size:.72rem;font-weight:700;text-transform:uppercase;letter-spacing:.04em; }
</style>
<div class="container" style="padding-top:1.5rem;padding-bottom:3rem;">
  <div class="dash-layout">
    <!-- SIDEBAR -->
    <aside class="dash-sidebar">
      <div class="profile-pic">
        <c:out value="${fn:toUpperCase(fn:substring(sessionScope.user.username,0,1))}"/>
      </div>
      <div class="profile-name"><c:out value="${sessionScope.user.username}"/></div>
      <div class="profile-email"><c:out value="${sessionScope.user.email}"/></div>
      <ul class="dash-nav">
        <li><a href="#orders" class="tab-link tab-active" data-tab="orders"><span class="ico">📦</span> My Orders</a></li>
        <li><a href="#profile" class="tab-link" data-tab="profile"><span class="ico">👤</span> Profile</a></li>
        <li><a href="${pageContext.request.contextPath}/shop" class=""><span class="ico">📚</span> Browse Books</a></li>
      </ul>
    </aside>
    <!-- MAIN CONTENT -->
    <div style="min-width:0;">
      <!-- ORDERS TAB -->
      <div id="tab-orders" class="tab-panel active">
        <div class="dash-card">
          <h2 style="font-size:1.05rem;color:var(--brand);margin-bottom:1.25rem;">📦 My Orders</h2>
          <c:choose>
            <c:when test="${empty myOrders}">
              <div class="empty-state" style="margin:0;">
                <div class="ico">📚</div>
                <h3>No orders yet</h3>
                <p>Browse our books and place your first order.</p>
                <a href="${pageContext.request.contextPath}/shop" class="btn btn-primary btn-sm" style="margin-top:.75rem;">Browse Books</a>
              </div>
            </c:when>
            <c:otherwise>
              <div style="display:grid;gap:.85rem;">
                <c:forEach var="o" items="${myOrders}">
                  <div style="border:1px solid var(--line);border-radius:var(--radius-lg);padding:1.1rem;
                               background:#fff;transition:box-shadow .15s;">
                    <div style="display:flex;justify-content:space-between;align-items:flex-start;
                                flex-wrap:wrap;gap:.5rem;margin-bottom:.75rem;">
                      <div>
                        <div style="font-weight:700;font-size:.95rem;">Order #${o.id}</div>
                        <div style="font-size:.78rem;color:var(--muted);margin-top:.15rem;">
                          <c:out value="${o.createdAtFormatted}"/>
                        </div>
                      </div>
                      <span class="order-badge order-status-${fn:toLowerCase(o.status)}">
                        <c:choose>
                          <c:when test="${o.status == 'PENDING'}">⏳ Pending</c:when>
                          <c:when test="${o.status == 'CONFIRMED'}">✅ Confirmed</c:when>
                          <c:when test="${o.status == 'DELIVERED'}">🚚 Delivered</c:when>
                          <c:when test="${o.status == 'CANCELLED'}">❌ Cancelled</c:when>
                          <c:otherwise><c:out value="${o.status}"/></c:otherwise>
                        </c:choose>
                      </span>
                    </div>
                    <div style="display:flex;align-items:center;gap:.75rem;padding:.75rem;
                                background:var(--bg-soft);border-radius:var(--radius);margin-bottom:.75rem;">
                      <span style="font-size:1.5rem;">📖</span>
                      <div style="flex:1;min-width:0;">
                        <div style="font-weight:600;font-size:.9rem;white-space:nowrap;overflow:hidden;text-overflow:ellipsis;">
                          <c:out value="${o.productName}"/>
                        </div>
                        <div style="font-size:.78rem;color:var(--muted);">
                          Qty: ${o.quantity} &times; $<fmt:formatNumber value="${o.unitPrice}" pattern="#,##0.00"/>
                        </div>
                      </div>
                      <div style="font-weight:800;color:var(--brand);font-size:1rem;white-space:nowrap;">
                        $<fmt:formatNumber value="${o.totalPrice}" pattern="#,##0.00"/>
                      </div>
                    </div>
                    <div style="font-size:.82rem;color:var(--muted);display:flex;gap:1.25rem;flex-wrap:wrap;">
                      <span>📍 <c:out value="${o.address}"/>, <c:out value="${o.city}"/></span>
                      <span>📞 <c:out value="${o.phone}"/></span>
                    </div>
                  </div>
                </c:forEach>
              </div>
            </c:otherwise>
          </c:choose>
        </div>
      </div>
      <!-- PROFILE TAB -->
      <div id="tab-profile" class="tab-panel">
        <div class="dash-card">
          <h2 style="font-size:1.05rem;color:var(--brand);margin-bottom:1.25rem;">👤 My Profile</h2>
          <dl style="display:grid;grid-template-columns:140px 1fr;gap:.65rem 1.25rem;margin:0;">
            <dt style="color:var(--muted);font-size:.85rem;">Username</dt>
            <dd style="margin:0;font-weight:600;font-size:.9rem;"><c:out value="${sessionScope.user.username}"/></dd>
            <dt style="color:var(--muted);font-size:.85rem;">Email</dt>
            <dd style="margin:0;font-weight:600;font-size:.9rem;"><c:out value="${sessionScope.user.email}"/></dd>
            <dt style="color:var(--muted);font-size:.85rem;">Role</dt>
            <dd style="margin:0;font-size:.9rem;"><c:out value="${sessionScope.user.role}"/></dd>
          </dl>
          <div style="margin-top:1.5rem;padding-top:1rem;border-top:1px solid var(--line);">
            <a href="${pageContext.request.contextPath}/account/delete" class="btn btn-danger btn-sm"
               onclick="return confirm('Are you sure you want to delete your account? This cannot be undone.');">
              Delete My Account
            </a>
          </div>
        </div>
      </div>
    </div>
  </div>
</div>
<script>
  function switchTab(name) {
    document.querySelectorAll('.tab-panel').forEach(p => p.classList.remove('active'));
    document.querySelectorAll('.tab-link').forEach(a => a.classList.remove('tab-active'));
    document.getElementById('tab-' + name).classList.add('active');
    var link = document.querySelector('.tab-link[data-tab="' + name + '"]');
    if (link) link.classList.add('tab-active');
    history.replaceState(null, '', '#' + name);
  }
  document.querySelectorAll('.tab-link').forEach(function(link) {
    link.addEventListener('click', function(e) {
      e.preventDefault();
      switchTab(this.dataset.tab);
    });
  });
  (function() {
    var hash = window.location.hash.replace('#', '');
    if (['orders', 'profile'].indexOf(hash) !== -1) switchTab(hash);
  })();
</script>
<%@ include file="_layout-footer.jspf" %>
