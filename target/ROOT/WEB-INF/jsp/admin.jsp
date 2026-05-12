<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<%@ taglib prefix="fmt" uri="jakarta.tags.fmt" %>
<%@ taglib prefix="fn" uri="jakarta.tags.functions" %>
<c:set var="pageTitle" value="Admin Dashboard" scope="request"/>
<%@ include file="_layout-header.jspf" %>
<style>
  .tab-panel { display: none; }
  .tab-panel.active { display: grid; gap: 1.25rem; }
  .dash-nav a.tab-active { background:#eff6ff; color:var(--brand); }
  .stat-row { display: grid; grid-template-columns: repeat(3,1fr); gap: 1rem; }
  @media(max-width:600px){ .stat-row { grid-template-columns: 1fr 1fr; } }
  .stat-mini { background:#fff; border:1px solid var(--line); border-radius:var(--radius-lg);
               padding:1.1rem; text-align:center; box-shadow:var(--shadow-sm); }
  .stat-mini .n { font-family:'Plus Jakarta Sans',sans-serif; font-size:1.9rem;
                  font-weight:800; color:var(--brand); line-height:1; }
  .stat-mini .l { font-size:.78rem; color:var(--muted); margin-top:.3rem; }
  .section-divider {
    display: flex; align-items: center; gap: .75rem;
    margin: .25rem 0 1rem;
  }
  .section-divider h2 { margin: 0; font-size: 1.05rem; white-space: nowrap; }
  .section-divider::after { content: ''; flex: 1; height: 1px; background: var(--line); }
</style>
<div class="container" style="padding-top:1.5rem;padding-bottom:3rem;">
  <div class="dash-layout">
    <!-- SIDEBAR -->
    <aside class="dash-sidebar">
      <div class="profile-pic">
        <c:out value="${fn:toUpperCase(fn:substring(sessionScope.user.username,0,1))}"/>
      </div>
      <div class="profile-name"><c:out value="${sessionScope.user.username}"/></div>
      <div class="profile-email" style="margin-bottom:.5rem;"><c:out value="${sessionScope.user.email}"/></div>
      <div style="text-align:center;margin-bottom:1rem;"><span class="role-pill">Admin</span></div>
      <ul class="dash-nav">
        <li><a href="#overview" class="tab-link tab-active" data-tab="overview"><span class="ico">📊</span> Overview</a></li>
        <li><a href="#products" class="tab-link" data-tab="products"><span class="ico">📚</span> Books</a></li>
        <li><a href="#users" class="tab-link" data-tab="users"><span class="ico">👥</span> Users</a></li>
        <li><a href="#orders" class="tab-link" data-tab="orders"><span class="ico">📦</span> Orders</a></li>
      </ul>
    </aside>
    <!-- MAIN CONTENT -->
    <div style="min-width:0;">
      <!-- OVERVIEW TAB -->
      <div id="tab-overview" class="tab-panel active">
        <div class="dash-card">
          <div class="section-divider"><h2>📊 Store Overview</h2></div>
          <div class="stat-row">
            <div class="stat-mini">
              <div class="n">${fn:length(products)}</div>
              <div class="l">Total Books</div>
            </div>
            <div class="stat-mini">
              <div class="n">${fn:length(users)}</div>
              <div class="l">Registered Users</div>
            </div>
            <div class="stat-mini">
              <div class="n">${fn:length(allOrders)}</div>
              <div class="l">Total Orders</div>
            </div>
          </div>
        </div>
        <div style="display:grid;grid-template-columns:1fr 1fr;gap:1rem;">
          <div class="dash-card" style="cursor:pointer;" onclick="switchTab('products')">
            <div style="font-size:2rem;margin-bottom:.5rem;">📚</div>
            <div style="font-weight:700;font-size:.95rem;">Manage Books</div>
            <div style="color:var(--muted);font-size:.82rem;margin-top:.25rem;">Add, edit or remove books</div>
            <div style="margin-top:.75rem;color:var(--brand);font-size:.85rem;font-weight:600;">Go to Books →</div>
          </div>
          <div class="dash-card" style="cursor:pointer;" onclick="switchTab('users')">
            <div style="font-size:2rem;margin-bottom:.5rem;">👥</div>
            <div style="font-weight:700;font-size:.95rem;">Manage Users</div>
            <div style="color:var(--muted);font-size:.82rem;margin-top:.25rem;">View and manage all accounts</div>
            <div style="margin-top:.75rem;color:var(--brand);font-size:.85rem;font-weight:600;">Go to Users →</div>
          </div>
        </div>
      </div>

      <!-- PRODUCTS TAB -->
      <div id="tab-products" class="tab-panel">
        <div class="dash-card">
          <div style="display:flex;align-items:center;justify-content:space-between;margin-bottom:1rem;flex-wrap:wrap;gap:.75rem;">
            <div class="section-divider" style="margin:0;flex:1;"><h2>📚 All Books (${fn:length(products)})</h2></div>
            <a href="${pageContext.request.contextPath}/products/new" class="btn btn-primary btn-sm">+ Add Book</a>
          </div>
          <c:choose>
            <c:when test="${empty products}">
              <div class="empty-state" style="margin:0;">
                <div class="ico">📚</div><p>No books yet. Add one above.</p>
              </div>
            </c:when>
            <c:otherwise>
              <div style="overflow-x:auto;">
                <table class="data-table">
                  <thead>
                    <tr>
                      <th>#</th>
                      <th>Title</th>
                      <th>Price</th>
                      <th>Stock</th>
                      <th>Actions</th>
                    </tr>
                  </thead>
                  <tbody>
                    <c:forEach var="p" items="${products}">
                      <tr>
                        <td style="color:var(--muted);font-size:.82rem;">${p.id}</td>
                        <td>
                          <div style="display:flex;align-items:center;gap:.6rem;">
                            <span style="font-size:1.2rem;">📖</span>
                            <div>
                              <a href="${pageContext.request.contextPath}/products/${p.id}"
                                 style="font-weight:600;color:var(--ink);">
                                <c:out value="${p.name}"/>
                              </a>
                              <c:if test="${not empty p.description}">
                                <div style="font-size:.75rem;color:var(--muted);margin-top:.15rem;
                                            white-space:nowrap;overflow:hidden;text-overflow:ellipsis;max-width:260px;">
                                  <c:out value="${fn:substring(p.description,0,60)}"/>…
                                </div>
                              </c:if>
                            </div>
                          </div>
                        </td>
                        <td style="font-weight:600;color:var(--brand);">
                          $<fmt:formatNumber value="${p.price}" pattern="#,##0.00"/>
                        </td>
                        <td>
                          <c:choose>
                            <c:when test="${p.stock == 0}">
                              <span class="stock-badge stock-out">0 — Out</span>
                            </c:when>
                            <c:when test="${p.stock < 10}">
                              <span class="stock-badge stock-low">${p.stock} — Low</span>
                            </c:when>
                            <c:otherwise>
                              <span class="stock-badge stock-in">${p.stock}</span>
                            </c:otherwise>
                          </c:choose>
                        </td>
                        <td>
                          <div style="display:flex;gap:.4rem;align-items:center;">
                            <a href="${pageContext.request.contextPath}/products/${p.id}/edit"
                               class="btn btn-ghost btn-sm">Edit</a>
                            <form method="post"
                                  action="${pageContext.request.contextPath}/products/${p.id}/delete"
                                  onsubmit="return confirm('Delete «${p.name}»?');"
                                  style="margin:0;">
                              <button type="submit" class="btn btn-danger btn-sm">Delete</button>
                            </form>
                          </div>
                        </td>
                      </tr>
                    </c:forEach>
                  </tbody>
                </table>
              </div>
            </c:otherwise>
          </c:choose>
        </div>
      </div>

      <!-- USERS TAB -->
      <div id="tab-users" class="tab-panel">
        <div class="dash-card">
          <div class="section-divider"><h2>👥 All Users (${fn:length(users)})</h2></div>
          <c:choose>
            <c:when test="${empty users}">
              <div class="empty-state" style="margin:0;">
                <div class="ico">👥</div><p>No users registered yet.</p>
              </div>
            </c:when>
            <c:otherwise>
              <div style="overflow-x:auto;">
                <table class="data-table">
                  <thead>
                    <tr>
                      <th>#</th>
                      <th>User</th>
                      <th>Email</th>
                      <th>Role</th>
                      <th>Joined</th>
                      <th>Action</th>
                    </tr>
                  </thead>
                  <tbody>
                    <c:forEach var="u" items="${users}">
                      <tr>
                        <td style="color:var(--muted);font-size:.82rem;">${u.id}</td>
                        <td>
                          <div style="display:flex;align-items:center;gap:.6rem;">
                            <div style="width:32px;height:32px;border-radius:50%;flex-shrink:0;
                                        background:linear-gradient(135deg,var(--brand),var(--accent));
                                        color:#fff;font-weight:700;font-size:.8rem;
                                        display:grid;place-items:center;">
                              <c:out value="${fn:toUpperCase(fn:substring(u.username,0,1))}"/>
                            </div>
                            <span style="font-weight:600;"><c:out value="${u.username}"/></span>
                          </div>
                        </td>
                        <td style="color:var(--ink-2);font-size:.87rem;">
                          <c:out value="${u.email}"/>
                        </td>
                        <td>
                          <c:choose>
                            <c:when test="${u.role == 'ADMIN'}">
                              <span class="role-pill">Admin</span>
                            </c:when>
                            <c:otherwise>
                              <span style="font-size:.78rem;color:var(--muted);
                                           background:var(--bg-soft);padding:2px 8px;
                                           border-radius:999px;border:1px solid var(--line);">
                                User
                              </span>
                            </c:otherwise>
                          </c:choose>
                        </td>
                        <td style="color:var(--muted);font-size:.82rem;">
                          <c:out value="${u.createdAtFormatted}"/>
                        </td>
                        <td>
                          <c:choose>
                            <c:when test="${u.role == 'ADMIN'}">
                              <span style="font-size:.78rem;color:var(--muted);">Protected</span>
                            </c:when>
                            <c:otherwise>
                              <form method="post"
                                    action="${pageContext.request.contextPath}/admin/delete-user"
                                    onsubmit="return confirm('Permanently delete user «${u.username}»? This will also remove their reviews.');"
                                    style="margin:0;">
                                <input type="hidden" name="userId" value="${u.id}"/>
                                <button type="submit" class="btn btn-danger btn-sm">Delete</button>
                              </form>
                            </c:otherwise>
                          </c:choose>
                        </td>
                      </tr>
                    </c:forEach>
                  </tbody>
                </table>
              </div>
            </c:otherwise>
          </c:choose>
        </div>
      </div>

      <!-- ORDERS TAB -->
      <div id="tab-orders" class="tab-panel">
        <div class="dash-card">
          <div class="section-divider"><h2>📦 All Orders (${fn:length(allOrders)})</h2></div>
          <c:choose>
            <c:when test="${empty allOrders}">
              <div class="empty-state" style="margin:0;"><div class="ico">📦</div><p>No orders placed yet.</p></div>
            </c:when>
            <c:otherwise>
              <div style="overflow-x:auto;">
                <table class="data-table">
                  <thead>
                    <tr><th>#</th><th>Customer</th><th>Book</th><th>Qty</th><th>Total</th><th>City</th><th>Status</th><th>Date</th></tr>
                  </thead>
                  <tbody>
                    <c:forEach var="o" items="${allOrders}">
                      <tr>
                        <td style="color:var(--muted);font-size:.8rem;">${o.id}</td>
                        <td style="font-weight:600;font-size:.87rem;"><c:out value="${o.fullName}"/><br/><span style="color:var(--muted);font-weight:400;font-size:.75rem;"><c:out value="${o.phone}"/></span></td>
                        <td style="font-size:.87rem;"><c:out value="${o.productName}"/></td>
                        <td style="text-align:center;">${o.quantity}</td>
                        <td style="font-weight:700;color:var(--brand);">$<fmt:formatNumber value="${o.totalPrice}" pattern="#,##0.00"/></td>
                        <td style="font-size:.85rem;"><c:out value="${o.city}"/></td>
                        <td>
                          <span style="padding:2px 8px;border-radius:999px;font-size:.72rem;font-weight:700;
                            background:${o.status=='PENDING'?'#fffbeb':o.status=='DELIVERED'?'#eff6ff':'#ecfdf5'};
                            color:${o.status=='PENDING'?'#92400e':o.status=='DELIVERED'?'#1e40af':'#065f46'};
                            border:1px solid ${o.status=='PENDING'?'#fde68a':o.status=='DELIVERED'?'#bfdbfe':'#a7f3d0'};">
                            <c:out value="${o.status}"/>
                          </span>
                        </td>
                        <td style="color:var(--muted);font-size:.78rem;white-space:nowrap;"><c:out value="${o.createdAtFormatted}"/></td>
                      </tr>
                    </c:forEach>
                  </tbody>
                </table>
              </div>
            </c:otherwise>
          </c:choose>
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
    if (['overview','products','users','orders'].indexOf(hash) !== -1) switchTab(hash);
  })();
</script>
<%@ include file="_layout-footer.jspf" %>
