<%@ page contentType="text/html;charset=UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="tags" tagdir="/WEB-INF/tags" %>
<%@ taglib prefix="frm" uri="http://java.sun.com/jsp/jstl/fmt" %>

<jsp:useBean id="product" type="com.es.phoneshop.model.product.Product" scope="request"/>
<tags:master pageTitle="Product Details">
    <c:if test="${not empty param.message}">
        <div class="message">
                ${param.message}
        </div>
    </c:if>
    <c:if test="${not empty error}">
        <div class="error">
            There was an error adding to cart
        </div>
    </c:if>
    <p>
            ${product.description}
    </p>
    <form method="post" action="${pageContext.request.contextPath}/products/${product.id}">
        <table>
            <tr>
                <td>Image</td>
                <td>
                    <img src="${product.imageUrl}">
                </td>
            </tr>
            <tr>
                <td>code</td>
                <td>
                        ${product.code}
                </td>
            </tr>
            <tr>
                <td>stock</td>
                <td>
                        ${product.stock}
                </td>
            </tr>
            <tr>
                <td>
                    price
                </td>
                <td class="price">
                    <frm:formatNumber value="${product.price}" type="currency"
                                      currencySymbol="${product.currency.symbol}"/>
                </td>
            </tr>
            <tr>
                <td class="quantity">quantity</td>
                <td>
                    <input name="quantity" value="${not empty error ? param.quantity : 1  }">
                    <c:if test="${not empty error}">
                        <div class="error">
                                ${error}
                        </div>
                    </c:if>
                </td>
            </tr>
        </table>
        <button>Add to Cart</button>
    </form>
    <jsp:include page="recentlyViewed.jsp"/>
</tags:master>