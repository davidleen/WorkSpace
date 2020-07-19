<%--
  Created by IntelliJ IDEA.
  User: davidleen29
  Date: 2015/5/14
  Time: 0:37
  To change this template use File | Settings | File Templates.
--%>
<!DOCTYPE html>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<script type="text/javascript" charset="utf8" src="https://ajax.aspnetcdn.com/ajax/jQuery/jquery-3.4.1.min.js"></script>
<script src="https://cdnjs.cloudflare.com/ajax/libs/jquery.form/4.2.2/jquery.form.min.js"></script>

<body>

<form method="POST" enctype="multipart/form-data" class="ajax_form"
>
	测试发送私信<br/> <br/>

	内容: <br/>
	<input id="id_msg" value=""
		   type="text" name="msg"/><br/>
	用户:<br/>
	<select  id="id_user"  name="user" style="width:200px">   </select>
	<br/>



	<br/>
	<button type="button" id="btn_send">发送</button>
</form>







<script type="text/javascript">




	function addItems(select)
	{


        var users=${users}
		var lenth=users.length;
		for (var i = 0; i < lenth; i++) {
			var option=document.createElement("option");
			option.text=users[i].code+"--"+users[i].name+"----"+users[i].chineseName;
			option.value=users[i].id;

			try{
				// 对于更早的版本IE8
				select.add(option,select.options[null]);
			}catch (e){
				select.add(option,null);
			}

		}

	}



	$(document).ready(function () {


		addItems(document.getElementById("id_user"))






		$("#btn_send").on('click', function() {
			var params = {
				type: 'POST',
				dataType: 'text',
				url: "${server}/jsp/test/sendMessage"
				,success: function(text, textStatus, xhr) {


					$("#id_msg").val("")
					// $("#id_platform").val("")


					   alert("提交成功")


				},
				error: function(xhr, textStatus, errorThrown) {

					alert(errorThrown)
				}
			}

			$(".ajax_form").ajaxSubmit(params);


		})



	});

</script>



</body>
</html>
