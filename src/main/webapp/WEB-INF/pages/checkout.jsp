<%@ page contentType="text/html;charset=UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="tags" tagdir="/WEB-INF/tags" %>
<link rel="stylesheet" href="${pageContext.servletContext.contextPath}/styles/priceHictory.css">

<jsp:useBean id="order" type="com.es.phoneshop.model.order.Order" scope="request"/>
<tags:master pageTitle="Checkout">
    <c:if test="${not empty errors}">
        <p class="error">
            Error occured while placing order
        </p>
    </c:if>
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

        </tr>
        </thead>
        <c:forEach var="item" items="${order.items}" varStatus="status">
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
                        ${item.quantity}
                </td>
                <td class="price">
                    <fmt:formatNumber value="${item.product.price}" type="currency"
                                      currencySymbol="${item.product.currency.symbol}"/>
                </td>
            </tr>
        </c:forEach>
        <tr>
            <td></td>
            <td></td>
            <td class="price">Subtotal:</td>
            <td>
                <fmt:formatNumber value="${order.subtotal}" type="currency"
                                  currencySymbol="${order.items.get(0).product.currency.symbol}"/>
            </td>
        </tr>
        <tr>
            <td></td>
            <td></td>
            <td class="price">Delivery cost:</td>
            <td>
                <fmt:formatNumber value="${order.deliveryCost}" type="currency"
                                  currencySymbol="${order.items.get(0).product.currency.symbol}"/>
            </td>
        </tr>
        <tr>
            <td></td>
            <td></td>
            <td class="price">Total cost:</td>
            <td>
                <fmt:formatNumber value="${order.totalCost}" type="currency"
                                  currencySymbol="${order.items.get(0).product.currency.symbol}"/>
            </td>
        </tr>

    </table>
    <h2>
        Your detais
    </h2>
    <form action="${pageContext.servletContext.contextPath}/checkout" method="post">
        <table>
            <tags:orderFormRow name="firstName" label="First Name" order="${order}"
                               errors="${errors}"></tags:orderFormRow>
            <tags:orderFormRow name="lastName" label="Last Name" order="${order}"
                               errors="${errors}"></tags:orderFormRow>
            <tags:orderFormRow name="phone" label="Phone" order="${order}" errors="${errors}"></tags:orderFormRow>
            <tr>
                <td>Delivery Date<samp style="color: red">*</samp></td>
                <td>
                    <input name="deliveryDate" type="date" min="${minDate}">
                    <div class="error">
                            ${errors["deliveryDate"]}
                    </div>
                </td>
            </tr>
            <tags:orderFormRow name="deliveryAddres" label="Delivery Addres" order="${order}"
                               errors="${errors}"></tags:orderFormRow>
            <tr>
                <td>Payment Method<samp style="color: red">*</samp></td>
                <td>
                    <select name="paymentMethod">
                        <c:forEach var="paymentMethod" items="${paymentMethods}">
                            <option>${paymentMethod}</option>
                        </c:forEach>
                    </select>
                </td>
            </tr>
        </table>
        <button> update</button>
    </form>
</tags:master>