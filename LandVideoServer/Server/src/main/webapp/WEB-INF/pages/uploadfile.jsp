<%--
  Created by IntelliJ IDEA.
  User: davidleen29
  Date: 2015/5/14
  Time: 0:37
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<body>
<form method="POST" enctype="multipart/form-data"
      action="/api/upload">
  File to upload: <input type="file" name="file"><br /> Name: <input
        type="text" name="name"><br /> <br /> <input type="submit"
                                                     value="Upload"> Press here to upload the file!
</form>
</body>
</html>
