<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<%@ taglib prefix="fmt" uri="jakarta.tags.fmt" %>
<%@ taglib prefix="fn" uri="jakarta.tags.functions" %>
<c:set var="pageTitle" value="Home" scope="request"/>
<%@ include file="_layout-header.jspf" %>
<div class="container">
  <c:if test="${not empty sessionScope.user}">
    <div style="background:#fff;border:1px solid var(--line);border-radius:var(--radius-lg);padding:1rem 1.5rem;margin-bottom:1.5rem;display:flex;align-items:center;justify-content:space-between;flex-wrap:wrap;gap:.75rem;box-shadow:var(--shadow-sm);">
      <div style="display:flex;align-items:center;gap:.85rem;">
        <div style="width:44px;height:44px;border-radius:50%;flex-shrink:0;background:linear-gradient(135deg,var(--brand),var(--accent));color:#fff;font-weight:800;font-size:1.1rem;display:grid;place-items:center;">
          <c:out value="${fn:toUpperCase(fn:substring(sessionScope.user.username,0,1))}"/>
        </div>
        <div>
          <div style="font-weight:700;font-size:.95rem;">Welcome back, <c:out value="${sessionScope.user.username}"/>!</div>
          <div style="font-size:.8rem;color:var(--muted);"><c:out value="${sessionScope.user.email}"/>
            <c:if test="${sessionScope.user.role == 'ADMIN'}">&nbsp;&middot;&nbsp;<span style="color:var(--brand);font-weight:600;">Administrator</span></c:if>
          </div>
        </div>
      </div>
      <div style="display:flex;gap:.6rem;flex-wrap:wrap;">
        <a href="${pageContext.request.contextPath}/shop" class="btn btn-primary btn-sm">Browse Books</a>
        <c:choose>
          <c:when test="${sessionScope.user.role == 'ADMIN'}"><a href="${pageContext.request.contextPath}/admin" class="btn btn-ghost btn-sm">Admin Dashboard</a></c:when>
          <c:otherwise><a href="${pageContext.request.contextPath}/account" class="btn btn-ghost btn-sm">My Dashboard</a></c:otherwise>
        </c:choose>
      </div>
    </div>
  </c:if>
  <section class="hero">
    <div class="hero-inner">
      <div>
        <span class="hero-eyebrow">📚 Programming Books Store</span>
        <h1>Level Up Your<br/>Coding Skills</h1>
        <p>Discover thousands of programming books — from beginner tutorials to advanced system design, algorithms, and beyond.</p>
        <div class="hero-cta">
          <a href="${pageContext.request.contextPath}/shop" class="btn btn-accent btn-lg">Browse Books</a>
          <a href="${pageContext.request.contextPath}/contact" class="btn btn-ghost btn-lg" style="background:rgba(255,255,255,.1);color:#fff;border-color:rgba(255,255,255,.25);">Contact Us</a>
        </div>
      </div>
      <div class="hero-visual">
        <div class="hero-card"><div class="label">🔥 Bestseller</div><div class="val">Clean Code — Robert C. Martin</div><div class="sub">$34.99 &middot; In Stock</div></div>
        <div class="hero-card"><div class="label">⭐ Top Rated</div><div class="val">The Pragmatic Programmer</div><div class="sub">$39.99 &middot; ★★★★★</div></div>
        <div class="hero-card"><div class="label">🆕 New Arrival</div><div class="val">Designing Data-Intensive Apps</div><div class="sub">$44.99 &middot; 2nd Edition</div></div>
      </div>
    </div>
  </section>
  <div class="stats-strip">
    <div class="stat-card"><div class="num">10K+</div><div class="lbl">Books Available</div></div>
    <div class="stat-card"><div class="num">50K+</div><div class="lbl">Happy Readers</div></div>
    <div class="stat-card"><div class="num">200+</div><div class="lbl">Publishers</div></div>
    <div class="stat-card"><div class="num">100%</div><div class="lbl">Genuine Titles</div></div>
  </div>
  <section style="margin-bottom:2.5rem;">
    <div class="section-head"><div><h2>Browse by Category</h2><p>Find books for every skill level</p></div></div>
    <div class="cat-grid">
      <a href="${pageContext.request.contextPath}/shop?cat=webdev" class="cat-card"><div class="ico">🌐</div><div class="name">Web Development</div></a>
      <a href="${pageContext.request.contextPath}/shop?cat=algorithms" class="cat-card"><div class="ico">🧮</div><div class="name">Algorithms &amp; DSA</div></a>
      <a href="${pageContext.request.contextPath}/shop?cat=devops" class="cat-card"><div class="ico">☁️</div><div class="name">DevOps &amp; Cloud</div></a>
      <a href="${pageContext.request.contextPath}/shop?cat=ai" class="cat-card"><div class="ico">🤖</div><div class="name">AI &amp; Machine Learning</div></a>
      <a href="${pageContext.request.contextPath}/shop?cat=security" class="cat-card"><div class="ico">🔐</div><div class="name">Cybersecurity</div></a>
      <a href="${pageContext.request.contextPath}/shop?cat=databases" class="cat-card"><div class="ico">🗄️</div><div class="name">Databases</div></a>
      <a href="${pageContext.request.contextPath}/shop?cat=mobile" class="cat-card"><div class="ico">📱</div><div class="name">Mobile Dev</div></a>
      <a href="${pageContext.request.contextPath}/shop" class="cat-card"><div class="ico">📚</div><div class="name">All Books</div></a>
    </div>
  </section>
  <section id="featured" style="margin-bottom:2.5rem;">
    <div class="section-head">
      <div><h2>Featured Books</h2><p>Our most popular titles</p></div>
      <a href="${pageContext.request.contextPath}/shop" class="section-link">View all &rarr;</a>
    </div>
    <c:choose>
      <c:when test="${empty products}">
        <div class="empty-state"><div class="ico">📚</div><h3>No books yet</h3><p>Check back soon.</p></div>
      </c:when>
      <c:otherwise>
        <ul class="card-grid">
          <c:forEach var="p" items="${products}" end="7">
            <c:set var="pid" value="${p.id}"/>
            <c:set var="r" value="${ratings[pid]}"/>
            <c:set var="avg" value="${empty r ? 0 : r.average}"/>
            <c:set var="cnt" value="${empty r ? 0 : r.count}"/>
            <li class="product-card">
              <a class="cover" href="${pageContext.request.contextPath}/products/${p.id}">
                <c:choose>
                  <c:when test="${not empty p.imageUrl}"><img src="${p.imageUrl}" alt="<c:out value='${p.name}'/>"/></c:when>
                  <c:otherwise><div class="placeholder">📖</div></c:otherwise>
                </c:choose>
              </a>
              <c:if test="${not empty sessionScope.user and sessionScope.user.role == 'ADMIN'}">
                <div class="admin-actions">
                  <a href="${pageContext.request.contextPath}/products/${p.id}/edit" class="btn btn-ghost btn-sm" style="background:#fff;">✎</a>
                  <form method="post" action="${pageContext.request.contextPath}/products/${p.id}/delete" onsubmit="return confirm('Delete this book?');">
                    <button type="submit" class="btn btn-danger btn-sm">✕</button>
                  </form>
                </div>
              </c:if>
              <div class="body">
                <h3><a href="${pageContext.request.contextPath}/products/${p.id}"><c:out value="${p.name}"/></a></h3>
                <div class="rating-line">
                  <c:choose>
                    <c:when test="${cnt == 0}"><span class="stars"><span class="empty">★★★★★</span></span><span>No reviews</span></c:when>
                    <c:otherwise>
                      <span class="stars"><c:forEach begin="1" end="5" var="i"><c:choose><c:when test="${i <= avg}">★</c:when><c:otherwise><span class="empty">★</span></c:otherwise></c:choose></c:forEach></span>
                      <strong><fmt:formatNumber value="${avg}" pattern="0.0"/></strong><span>(${cnt})</span>
                    </c:otherwise>
                  </c:choose>
                </div>
                <div class="price-row">
                  <span class="price">$<fmt:formatNumber value="${p.price}" pattern="#,##0.00"/></span>
                  <c:choose>
                    <c:when test="${p.stock == 0}"><span class="stock-badge stock-out">Out of stock</span></c:when>
                    <c:when test="${p.stock < 10}"><span class="stock-badge stock-low">Low stock</span></c:when>
                    <c:otherwise><span class="stock-badge stock-in">In stock</span></c:otherwise>
                  </c:choose>
                </div>
              </div>
            </li>
          </c:forEach>
        </ul>
      </c:otherwise>
    </c:choose>
  </section>
  <section style="margin-bottom:2.5rem;">
    <div class="section-head"><div><h2>Why Choose CodeShelf?</h2><p>The best place for developer books</p></div></div>
    <div class="trust-strip">
      <div class="item"><div class="icon">✅</div><div><strong>Curated Selection</strong><span>Hand-picked titles by senior developers.</span></div></div>
      <div class="item"><div class="icon">🚚</div><div><strong>Fast Shipping</strong><span>Same-day dispatch on orders before 3 PM.</span></div></div>
      <div class="item"><div class="icon">💡</div><div><strong>Expert Picks</strong><span>Recommendations from industry professionals.</span></div></div>
      <div class="item"><div class="icon">🔒</div><div><strong>Secure Checkout</strong><span>Your data is always safe with us.</span></div></div>
    </div>
  </section>
  <c:if test="${not empty recentReviews}">
  <section style="margin-bottom:2.5rem;">
    <div class="section-head"><div><h2>Reader Reviews</h2><p>What our customers say</p></div></div>
    <div class="reviews-block" style="margin-top:0;">
      <ul class="review-list">
        <c:forEach var="rv" items="${recentReviews}">
          <li class="review-item">
            <div class="review-head">
              <div class="avatar"><c:out value="${fn:toUpperCase(fn:substring(rv.username,0,1))}"/></div>
              <div>
                <div class="who"><c:out value="${rv.username}"/></div>
                <div class="when">
                  <span class="stars"><c:forEach begin="1" end="5" var="i"><c:choose><c:when test="${i <= rv.rating}">★</c:when><c:otherwise><span class="empty">★</span></c:otherwise></c:choose></c:forEach></span>
                  &middot; <a href="${pageContext.request.contextPath}/products/${rv.productId}">book #${rv.productId}</a>
                  <c:if test="${not empty rv.createdAtFormatted}"> &middot; <c:out value="${rv.createdAtFormatted}"/></c:if>
                </div>
              </div>
            </div>
            <c:if test="${not empty rv.comment}"><p class="review-body"><c:out value="${rv.comment}"/></p></c:if>
          </li>
        </c:forEach>
      </ul>
    </div>
  </section>
  </c:if>
</div>
<%@ include file="_layout-footer.jspf" %>
