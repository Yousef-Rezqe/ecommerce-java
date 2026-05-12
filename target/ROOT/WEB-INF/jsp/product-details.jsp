<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<%@ taglib prefix="fmt" uri="jakarta.tags.fmt" %>
<%@ taglib prefix="fn" uri="jakarta.tags.functions" %>
<c:set var="pageTitle" value="${product.name}" scope="request"/>
<%@ include file="_layout-header.jspf" %>
<div class="container">
  <nav class="breadcrumb">
    <a href="${pageContext.request.contextPath}/home">Home</a><span class="sep">/</span>
    <a href="${pageContext.request.contextPath}/shop">Books</a><span class="sep">/</span>
    <span><c:out value="${product.name}"/></span>
  </nav>
  <article class="detail-grid">
    <div class="gallery">
      <c:choose>
        <c:when test="${not empty product.imageUrl}"><img src="${product.imageUrl}" alt="<c:out value='${product.name}'/>"/></c:when>
        <c:otherwise><div class="placeholder">📖</div></c:otherwise>
      </c:choose>
    </div>
    <div class="detail-info">
      <div class="detail-meta">
        <c:choose>
          <c:when test="${product.stock == 0}"><span class="stock-badge stock-out">Out of stock</span></c:when>
          <c:when test="${product.stock < 10}"><span class="stock-badge stock-low">Only ${product.stock} left</span></c:when>
          <c:otherwise><span class="stock-badge stock-in">In stock</span></c:otherwise>
        </c:choose>
        <c:if test="${rating.count > 0}">
          <span class="rating-line">
            <span class="stars"><c:forEach begin="1" end="5" var="i"><c:choose><c:when test="${i <= rating.average}">★</c:when><c:otherwise><span class="empty">★</span></c:otherwise></c:choose></c:forEach></span>
            <strong><fmt:formatNumber value="${rating.average}" pattern="0.0"/></strong>
            <a href="#reviews" style="color:var(--muted);">(${rating.count} review<c:if test="${rating.count != 1}">s</c:if>)</a>
          </span>
        </c:if>
      </div>
      <h1><c:out value="${product.name}"/></h1>
      <div class="price-row">
        <span class="price price-lg">$<fmt:formatNumber value="${product.price}" pattern="#,##0.00"/></span>
        <span class="muted">incl. tax</span>
      </div>
      <p class="desc">
        <c:choose>
          <c:when test="${not empty product.description}"><c:out value="${product.description}"/></c:when>
          <c:otherwise>A must-read programming book from CodeShelf's curated collection.</c:otherwise>
        </c:choose>
      </p>
      <ul class="feature-list">
        <li>Curated by senior developers</li>
        <li>Free shipping on orders over $35</li>
        <li>30-day return policy</li>
      </ul>
      <div class="cta-row">
        <c:choose>
          <c:when test="${not empty sessionScope.user and sessionScope.user.role != 'ADMIN'}">
            <a href="${pageContext.request.contextPath}/order?productId=${product.id}&qty=1"
               class="btn btn-primary btn-lg"
               <c:if test="${product.stock == 0}">style="pointer-events:none;opacity:.5;"</c:if>>
              🛒 Order Now
            </a>
          </c:when>
          <c:when test="${empty sessionScope.user}">
            <a href="${pageContext.request.contextPath}/login" class="btn btn-primary btn-lg">
              Sign in to Order
            </a>
          </c:when>
        </c:choose>
        <a href="${pageContext.request.contextPath}/shop" class="btn btn-ghost btn-lg">← Back to Books</a>
        <c:if test="${not empty sessionScope.user and sessionScope.user.role == 'ADMIN'}">
          <a href="${pageContext.request.contextPath}/products/${product.id}/edit" class="btn btn-ghost btn-lg">Edit</a>
          <form method="post" action="${pageContext.request.contextPath}/products/${product.id}/delete" onsubmit="return confirm('Delete this book?');">
            <button type="submit" class="btn btn-danger btn-lg">Delete</button>
          </form>
        </c:if>
      </div>
    </div>
  </article>
  <section class="reviews-block" id="reviews">
    <h2 style="margin-top:0;">Reader Reviews</h2>
    <div class="reviews-head">
      <div class="score-card">
        <c:choose>
          <c:when test="${rating.count == 0}">
            <div class="number">—</div><div class="stars"><span class="empty">★★★★★</span></div><div class="count">No reviews yet</div>
          </c:when>
          <c:otherwise>
            <div class="number"><fmt:formatNumber value="${rating.average}" pattern="0.0"/></div>
            <div class="stars"><c:forEach begin="1" end="5" var="i"><c:choose><c:when test="${i <= rating.average}">★</c:when><c:otherwise><span class="empty">★</span></c:otherwise></c:choose></c:forEach></div>
            <div class="count">Based on ${rating.count} review<c:if test="${rating.count != 1}">s</c:if></div>
          </c:otherwise>
        </c:choose>
      </div>
      <div class="bar-rows">
        <c:forEach var="star" begin="1" end="5">
          <c:set var="bucket" value="${5-(star-1)}"/>
          <c:set var="bc" value="0"/>
          <c:forEach var="rv" items="${reviews}"><c:if test="${rv.rating == bucket}"><c:set var="bc" value="${bc+1}"/></c:if></c:forEach>
          <c:set var="pct" value="${rating.count == 0 ? 0 : (bc*100)/rating.count}"/>
          <div class="bar-row">
            <span class="label">${bucket} star<c:if test="${bucket!=1}">s</c:if></span>
            <span class="track"><span class="fill" style="width:<fmt:formatNumber value='${pct}' pattern='0'/>%;"></span></span>
            <span class="count">${bc}</span>
          </div>
        </c:forEach>
      </div>
    </div>
    <c:choose>
      <c:when test="${empty reviews}">
        <div class="empty-state" style="margin:0;"><div class="ico">💬</div><h3>Be the first to review</h3></div>
      </c:when>
      <c:otherwise>
        <ul class="review-list">
          <c:forEach var="r" items="${reviews}">
            <li class="review-item">
              <div class="review-head">
                <div class="avatar"><c:out value="${fn:toUpperCase(fn:substring(r.username,0,1))}"/></div>
                <div>
                  <div class="who"><c:out value="${r.username}"/></div>
                  <div class="when">
                    <span class="stars"><c:forEach begin="1" end="5" var="i"><c:choose><c:when test="${i<=r.rating}">★</c:when><c:otherwise><span class="empty">★</span></c:otherwise></c:choose></c:forEach></span>
                    <c:if test="${not empty r.createdAtFormatted}"> &middot; <c:out value="${r.createdAtFormatted}"/></c:if>
                  </div>
                </div>
              </div>
              <c:if test="${not empty r.comment}"><p class="review-body"><c:out value="${r.comment}"/></p></c:if>
            </li>
          </c:forEach>
        </ul>
      </c:otherwise>
    </c:choose>
    <c:choose>
      <c:when test="${not empty sessionScope.user}">
        <form method="post" action="${pageContext.request.contextPath}/reviews" class="review-form">
          <input type="hidden" name="productId" value="${product.id}"/>
          <input type="hidden" name="redirect" value="/products/${product.id}#reviews"/>
          <h3 style="margin-top:0;">Write a Review</h3>
          <p class="muted" style="margin-top:0;">Posting as <strong><c:out value="${sessionScope.user.username}"/></strong></p>
          <div class="field">
            <label>Rating</label>
            <div class="rating-input">
              <input type="radio" id="r5" name="rating" value="5" checked/><label for="r5" title="5 stars">★</label>
              <input type="radio" id="r4" name="rating" value="4"/><label for="r4" title="4 stars">★</label>
              <input type="radio" id="r3" name="rating" value="3"/><label for="r3" title="3 stars">★</label>
              <input type="radio" id="r2" name="rating" value="2"/><label for="r2" title="2 stars">★</label>
              <input type="radio" id="r1" name="rating" value="1"/><label for="r1" title="1 star">★</label>
            </div>
          </div>
          <div class="field"><label>Comment</label><textarea name="comment" rows="3" maxlength="1000" placeholder="Share your thoughts on this book..."></textarea></div>
          <button type="submit" class="btn btn-primary">Post Review</button>
        </form>
      </c:when>
      <c:otherwise>
        <div class="review-form" style="text-align:center;">
          <p><strong>Want to review this book?</strong></p>
          <a href="${pageContext.request.contextPath}/login" class="btn btn-primary">Sign in to review</a>
        </div>
      </c:otherwise>
    </c:choose>
  </section>
</div>
<%@ include file="_layout-footer.jspf" %>
