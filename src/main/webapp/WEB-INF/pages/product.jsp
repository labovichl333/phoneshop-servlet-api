<%@ page contentType="text/html;charset=UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="tags" tagdir="/WEB-INF/tags" %>
<%@ taglib prefix="frm" uri="http://java.sun.com/jsp/jstl/fmt" %>

<jsp:useBean id="product" type="com.es.phoneshop.model.product.Product" scope="request"/>
<tags:master pageTitle="Product Details">
    <p>
        ${product.description}
    </p>
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
            <td>price</td>
            <td>
               <frm:formatNumber value="${product.price}" type="currency" currencySymbol="${product.currency.symbol}"/>
            </td>
        </tr>
    </table>
</tags:master>