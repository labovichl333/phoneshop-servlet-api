<%@ page contentType="text/html;charset=UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="tags" tagdir="/WEB-INF/tags" %>
<link rel="stylesheet" href="${pageContext.servletContext.contextPath}/styles/priceHictory.css">
<link rel="stylesheet" href="${pageContext.servletContext.contextPath}/styles/recentlyViewed.css">
<c:if test="${ViewedProductsHolder.getProducts().size()!=0}">
    <h1>
        Recently viewed
    </h1>
</c:if>
<p>
<table>
    <c:forEach var="product" items="${ViewedProductsHolder.getProducts()}">
        <div id="line_block">
            <img class="product-tile" src="${product.imageUrl}">
            <p>
                <a href="${pageContext.servletContext.contextPath}/products/${product.id}">
                        ${product.description}
                </a>
            </p>
            <p>
                <fmt:formatNumber value="${product.price}" type="currency"
                                  currencySymbol="${product.currency.symbol}"/>
            </p>
        </div>
    </c:forEach>
</table>
</p>

