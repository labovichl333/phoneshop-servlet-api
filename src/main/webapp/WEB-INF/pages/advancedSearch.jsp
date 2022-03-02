<%@ page contentType="text/html;charset=UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="tags" tagdir="/WEB-INF/tags" %>
<link rel="stylesheet" href="${pageContext.servletContext.contextPath}/styles/priceHictory.css">


<tags:master pageTitle="Advanced search Page">
    <h1>Advanced search Page</h1>
    <c:if test="${not empty errors}">
        <p class="error">
            There was a search error
        </p>
    </c:if>
    <c:if test="${not empty errors['minMaxPrice']}">
        <p class="error">
                ${errors['minMaxPrice']}
        </p>
    </c:if>
    <form>
        <p>
            Product code
            <input name="productCode" value="${param.productCode}">
        </p>
        <p>
            Min price
            <input name="minPrice" value="${param.minPrice}">
            <c:if test="${not empty errors['minPrice']}">
        <div class="error">
                ${errors['minPrice']}
        </div>
        </c:if>
        </p>
        <p>
            Max price
            <input name="maxPrice" value="${param.maxPrice}">
            <c:if test="${not empty errors['maxPrice']}">
        <div class="error">
                ${errors['maxPrice']}
        </div>
        </c:if>
        </p>
        <p>
            Min stock
            <input name="minStock" value="${param.minStock}">
            <c:if test="${not empty errors['minStock']}">
        <div class="error">
                ${errors['minStock']}
        </div>
        </c:if>
        </p>
        <button>Search</button>
    </form>
    <c:if test="${not empty products}">
        <p class="message">
            Found ${products.size()} products
        </p>
        <table>
            <thead>
            <tr>
                <td>Image</td>
                <td>
                    Description
                </td>

                <td class="price">
                    Price
                </td>
            </tr>
            </thead>
            <c:forEach var="product" items="${products}">
                <tr>
                    <td>
                        <img class="product-tile" src="${product.imageUrl}">
                    </td>
                    <td>
                        <a href="${pageContext.servletContext.contextPath}/products/${product.id}">
                                ${product.description}
                        </a>
                    </td>
                    <td class="price">
                        <div id="container">
                            <div class="link">
                                <a href="">
                                    <fmt:formatNumber value="${product.price}" type="currency"
                                                      currencySymbol="${product.currency.symbol}"/>
                                </a>

                                <span class="tip">
                                 <h2>Price History</h2>
                                    <h3> ${product.description}</h3>
                                    <table>
                                        <thread>
                                            <tr>
                                                <td>Start date</td>
                                                <td>Price</td>
                                            </tr>
                                        </thread>

                                <c:forEach var="priceHistory" items="${product.priceHistory}">
                                    <tr>
                                        <td>
                                                ${priceHistory.startDate}
                                        </td>
                                        <td>
                                            <fmt:formatNumber value="${priceHistory.price}" type="currency"
                                                              currencySymbol="${product.currency.symbol}"/>
                                        </td>
                                    </tr>
                                </c:forEach>
                                    </table>
                             </span>
                            </div>
                        </div>

                    </td>
                </tr>
            </c:forEach>
        </table>
    </c:if>
    <c:if test="${empty products}">
        <p class="message">
            Sorry, nothing was found.
        </p>
    </c:if>
</tags:master>