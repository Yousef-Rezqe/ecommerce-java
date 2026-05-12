<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<c:set var="pageTitle" value="Contact Us" scope="request"/>
<%@ include file="_layout-header.jspf" %>
<div class="page-hero">
  <div class="container">
    <h1>📬 Contact Us</h1>
    <p>We're here to help. Reach out anytime.</p>
  </div>
</div>
<div class="container">
  <div class="contact-grid">
    <div class="contact-info">
      <h2>Get in Touch</h2>
      <p>Our support team is available to help you find the right books and answer any questions.</p>
      <br/>
      <div class="contact-item">
        <span class="ico">📍</span>
        <div><strong>Address</strong><span>42 Developer Lane, Tech District, City 10001</span></div>
      </div>
      <div class="contact-item">
        <span class="ico">📞</span>
        <div><strong>Phone</strong><span>+1 (800) 555-CODE</span></div>
      </div>
      <div class="contact-item">
        <span class="ico">✉️</span>
        <div><strong>Email</strong><span>support@codeshelf.com</span></div>
      </div>
      <div class="contact-item">
        <span class="ico">🕐</span>
        <div><strong>Hours</strong><span>Mon–Fri 9AM–6PM<br/>Weekend support via email</span></div>
      </div>
    </div>
    <div class="contact-form-card">
      <h2 style="margin-bottom:1.25rem;">Send a Message</h2>
      <c:if test="${not empty success}"><div class="banner-success">✅ <c:out value="${success}"/></div></c:if>
      <c:if test="${not empty error}"><div class="banner-error"><c:out value="${error}"/></div></c:if>
      <form method="post" action="${pageContext.request.contextPath}/contact">
        <div class="field-row">
          <div class="field"><label>First Name</label><input name="firstName" required placeholder="John"/></div>
          <div class="field"><label>Last Name</label><input name="lastName" required placeholder="Doe"/></div>
        </div>
        <div class="field"><label>Email</label><input type="email" name="email" required placeholder="you@example.com"/></div>
        <div class="field"><label>Subject</label>
          <select name="subject">
            <option>General Inquiry</option>
            <option>Order Support</option>
            <option>Book Recommendation</option>
            <option>Shipping Question</option>
            <option>Returns &amp; Refunds</option>
          </select>
        </div>
        <div class="field"><label>Message</label><textarea name="message" rows="5" required placeholder="How can we help you?"></textarea></div>
        <button type="submit" class="btn btn-primary btn-block btn-lg">Send Message</button>
      </form>
    </div>
  </div>
</div>
<%@ include file="_layout-footer.jspf" %>
