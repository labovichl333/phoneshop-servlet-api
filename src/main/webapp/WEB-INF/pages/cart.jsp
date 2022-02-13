<%@ page contentType="text/html;charset=UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="tags" tagdir="/WEB-INF/tags" %>
<link rel="stylesheet" href="${pageContext.servletContext.contextPath}/styles/priceHictory.css">

<jsp:useBean id="cart" type="com.es.phoneshop.model.product.cart.Cart" scope="session"/>
<tags:master pageTitle="Cart">
    <c:if test="${not empty errors}">
        <p class="error">
            Errors in the update
        </p>
    </c:if>
    <c:if test="${not empty param.message}">
        <p class="message">
            All updated successfully
        </p>
    </c:if>
    <c:if test="${empty cart.items}">
        Cart is empry
    </c:if>
    <c:if test="${ not empty cart.items}">
        <form method="post" action="${pageContext.request.contextPath}/cart">
            <table>
                <thead>
                <tr>
                    <td>Image</td>
                    <td>
                        Description
                    </td>

                    <td class="quantity">
                        Quantity
                    </td>

                    <td class="price">
                        Price
                    </td>
                    <td>
                        Delete
                    </td>

                </tr>
                </thead>
                <c:forEach var="item" items="${cart.items}" varStatus="status">
                    <tr>
                        <td>
                            <img class="product-tile" src="${item.product.imageUrl}">
                        </td>
                        <td>
                            <a href="${pageContext.servletContext.contextPath}/products/${item.product.id}">
                                    ${item.product.description}
                            </a>
                        </td>
                        <td class="quantity">
                            <c:set var="error" value="${errors[item.product.id]}"/>
                            <input type="text" name="quantity"
                                   value="${not empty error ? paramValues['quantity'][status.index] : item.quantity}"
                                   class="quantity">
                            <input type="hidden" name="productId" value="${item.product.id}">
                            <c:if test="${not empty error}">
                                <div class="error">
                                        ${error}
                                </div>
                            </c:if>
                        </td>
                        <td class="price">
                            <fmt:formatNumber value="${item.product.price}" type="currency"
                                              currencySymbol="${item.product.currency.symbol}"/>
                            </div>
                        </td>
                        <td>
                            <button form="deleteCartItem"
                                    formaction="${pageContext.request.contextPath}/cart/deleteCartItem/${item.product.id}">
                                Delete
                            </button>
                        </td>
                    </tr>
                </c:forEach>
                <tr>
                    <td>
                    </td>
                    <td>
                    </td>
                    <td class="quantity">
                        Total quantity: ${cart.totalQuantity}
                    </td>
                    <td>
                        Total cost: <fmt:formatNumber value="${cart.totalCost   }" type="currency"
                                                      currencySymbol="${cart.items.get(0).product.currency.symbol}"/>
                    </td>
                    <td>
                    </td>
                </tr>
            </table>
            <p>
                <button>Update</button>
            </p>
        </form>
    </c:if>
    <form id="deleteCartItem" method="post">
    </form>
</tags:master>