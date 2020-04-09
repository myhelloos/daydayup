<%--
  Created by IntelliJ IDEA.
  User: alfred
  Date: 2020/4/9
  Time: 2:25 PM
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="j" uri="/jodd" %>
<html>
<body>
<h1>Messages</h1>

<ul>
    <j:iter items="${messages}" var="msg">
        <li>${msg.messageId} ${msg.text}
            <ul>
                <j:iter items="${msg.responses}" var="resp">
                    <li>${resp.responseId} ${resp.text}</li>
                </j:iter>
            </ul>
        </li>
    </j:iter>
</ul>

</body>
</html>