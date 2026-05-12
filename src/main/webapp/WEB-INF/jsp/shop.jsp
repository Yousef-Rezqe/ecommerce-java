<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<%@ taglib prefix="fmt" uri="jakarta.tags.fmt" %>
<%@ taglib prefix="fn" uri="jakarta.tags.functions" %>
<c:set var="pageTitle" value="Books" scope="request"/>
<%@ include file="_layout-header.jspf" %>
<div class="page-hero">
  <div class="container"><h1>📚 Browse Books</h1><p>Explore our full collection of programming and tech titles</p></div>
</div>
<div class="container">
  <div class="shop-layout">
    <aside class="filter-panel">
      <h3>🔍 Filter Books</h3>
      <form method="get" action="${pageContext.request.contextPath}/shop" id="filterForm">
        <div class="filter-section">
          <h4>Search</h4>
          <input type="text" name="q" value="<c:out value='${param.q}'/>" placeholder="Title, author, topic..."/>
        </div>
        <div class="filter-section">
          <h4>Price Range</h4>
          <div class="field-row" style="gap:.5rem;">
            <div class="field" style="margin:0;"><input type="number" name="minPrice" placeholder="Min $" value="${param.minPrice}" min="0" step="0.01"/></div>
            <div class="field" style="margin:0;"><input type="number" name="maxPrice" placeholder="Max $" value="${param.maxPrice}" min="0" step="0.01"/></div>
          </div>
        </div>
        <div class="filter-section">
          <h4>Availability</h4>
          <label style="display:flex;align-items:center;gap:.5rem;cursor:pointer;font-size:.87rem;">
            <input type="checkbox" name="inStock" value="1" ${param.inStock == '1' ? 'checked' : ''} style="width:auto;" onchange="document.getElementById('filterForm').submit()"/>
            In stock only
          </label>
        </div>
        <button type="submit" class="btn btn-primary btn-block">Apply Filters</button>
        <a href="${pageContext.request.contextPath}/shop" class="btn btn-ghost btn-block" style="margin-top:.5rem;">Clear All</a>
      </form>
    </aside>
    <div>
      <div style="display:flex;align-items:center;justify-content:space-between;margin-bottom:1rem;flex-wrap:wrap;gap:.5rem;">
        <p style="margin:0;color:var(--muted);font-size:.88rem;">
          <strong style="color:var(--ink);">${fn:length(products)}</strong> book<c:if test="${fn:length(products) != 1}">s</c:if> found
          <c:if test="${not empty param.q}"> for "<c:out value='${param.q}'/>"</c:if>
        </p>
      </div>
      <c:choose>
        <c:when test="${empty products}">
          <div class="empty-state"><div class="ico">🔍</div><h3>No books found</h3><p>Try adjusting your search or filters.</p><a href="${pageContext.request.contextPath}/shop" class="btn btn-primary" style="margin-top:.75rem;">Clear filters</a></div>
        </c:when>
        <c:otherwise>
          <ul class="card-grid">
            <c:forEach var="p" items="${products}">
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
                    <form method="post" action="${pageContext.request.contextPath}/products/${p.id}/delete" onsubmit="return confirm('Delete?');">
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
                  <c:if test="${not empty sessionScope.user and sessionScope.user.role != 'ADMIN' and p.stock > 0}">
                    <a href="${pageContext.request.contextPath}/order?productId=${p.id}&qty=1" class="btn btn-primary btn-block btn-sm" style="margin-top:.6rem;">🛒 Order Now</a>
                  </c:if>
                </div>
              </li>
            </c:forEach>
          </ul>
        </c:otherwise>
      </c:choose>
    </div>
  </div>
</div>
<%@ include file="_layout-footer.jspf" %>
