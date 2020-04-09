<%--
  Created by IntelliJ IDEA.
  User: alfred
  Date: 2020/4/9
  Time: 2:25 PM
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<body>
<h1>Add Message</h1>

<form action="/message.add" method="POST">
    <textarea name="message.text" rows="5" cols="50">
    </textarea>
    <button type="submit">Submit</button>
</form>

</body>
</html>
