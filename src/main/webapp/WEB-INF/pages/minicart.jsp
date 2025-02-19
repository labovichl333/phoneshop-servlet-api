<%@ page contentType="text/html;charset=UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="tags" tagdir="/WEB-INF/tags" %>
<link rel="stylesheet" href="${pageContext.servletContext.contextPath}/styles/priceHictory.css">

<jsp:useBean id="cart" type="com.es.phoneshop.model.cart.Cart" scope="request"/>
<c:if test="${empty cart.items}">
    Cart is empty
</c:if>
<c:if test="${not empty cart.items}">
    <a href="${pageContext.servletContext.contextPath}/cart">
        Cart: ${cart.totalQuantity} items.
        Total cost: <fmt:formatNumber value="${cart.totalCost   }" type="currency"
                                      currencySymbol="${cart.items.get(0).product.currency.symbol}"/>
    </a>
</c:if>

