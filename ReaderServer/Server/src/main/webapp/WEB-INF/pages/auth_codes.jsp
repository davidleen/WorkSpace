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
<link rel="stylesheet" type="text/css" href="https://cdn.datatables.net/1.10.20/css/jquery.dataTables.css">

<script type="text/javascript" charset="utf8" src="https://cdn.datatables.net/1.10.20/js/jquery.dataTables.js"></script>
<body>

<form method="POST" enctype="multipart/form-data" class="ajax_form"
>
    Add Codes <br/> <br/>

     Code: <br/>
    <input id="id_code" value=""
        type="text" name="code"/><br/>
    Platform:<br/>
    <select  id="id_platform"  name="platform" style="width:100px">   </select>
        <br/>



    <br/>
    <button type="button" id="btn_submit">提交</button>
</form>



<br/>
<br/>
<form method="POST" enctype="multipart/form-data"  id="form_rate"
>
    触发几率:${rate}%<br/>
    0<input id="id_rate" value="${rate}"
           type="range" name="rate"/>100<br/>
    <br/>
    <button type="button" id="btn_rate_submit">提交</button>
</form>



<br/>
<br/>
<br/>




平台选择:
<select  id="search_platform"   style="width:100px">
    <option></option>
</select>
<button type="button" id="btn_reload">搜索</button>
<table id="example" class="display" style="width:100%">
</table>
<br/>
<br/>

口令被请求总次数: ${requestCodeTime}<br/>
今日请求次数: ${requestCodeTimeToday}<br/>


<script type="text/javascript">

    var platid=   [1,2,3]
    var platname=  ["淘口令","快手","抖音"]

    function deleteRow( _id)
    {

        $.post("${server}/api/deleteAuthCode?id="+_id+"",null,function(result){
            $('#example').DataTable().ajax.reload(); // reload
        });



    }

    function addItems(select)
    {



        var lenth=platid.length;
        for (var i = 0; i < lenth; i++) {
            var option=document.createElement("option");
            option.text=platname[i];
            option.value=platid[i];

            try{
                // 对于更早的版本IE8
                select.add(option,select.options[null]);
            }catch (e){
                select.add(option,null);
            }

        }

    }



    $(document).ready(function () {


        addItems(document.getElementById("id_platform"))
        var  serach_platform=document.getElementById("search_platform")
        addItems(serach_platform)


        var exampleTable=     $('#example').DataTable({
            "processing": true,
            "serverSide": true
              , "paging": false
            , "ordering": false
            , "searching": true
            , "ajax":
                {
                    url: "${server}/api/authcodes"
                    , dataSrc: function (json) {
                        return json.data
                    },
                    "data": function ( d ) {


                        var sel=serach_platform.options[serach_platform.selectedIndex]
                        d.platform= sel.value
                    }

                }


            , columns: [
                {
                    title: "口令"
                    , "data": "code"
                    , width: "20%"
                }
                , {
                    title: "平台"
                    , "data": "platform"
                    , width: "20%"
                    , "render": function ( data, type, row, meta)
                    {
                        var lenth=platid.length;
                        for (var i = 0; i < lenth; i++) {
                            if(data==platid[i])return platname[i]
                        }

                         return data
                    }
                } , {
                    title: ""
                    , width: "20%"
                    ,data:""
                    , defaultContent: ""
                    , "render": function ( data, type, row, meta ) {
                        var content = '<a href="#" onclick=deleteRow('+row.id+') >删除</a>';
                        return content;
                    }
                }
               , {
                    title: " "
                    , defaultContent: ""

                }


            ]
        });




            $("#btn_submit").on('click', function() {
                var params = {
                    type: 'POST',
                    dataType: 'text',
                    url: "${server}/api/addCode"
                    ,success: function(text, textStatus, xhr) {


                        $("#id_code").val("")
                        // $("#id_platform").val("")
                        exampleTable.ajax.reload(function ( json ) {
                           //这里的json返回的是服务器的数据

                        } , false)

                      //  alert("提交成功")


                    },
                    error: function(xhr, textStatus, errorThrown) {

                        alert(errorThrown)
                    }
                }

                $(".ajax_form").ajaxSubmit(params);


            })
        $("#btn_rate_submit").on('click', function() {
            var params = {
                type: 'POST',
                dataType: 'text',
                url: "${server}/api/setRate"
                ,success: function(text, textStatus, xhr) {

                     alert("提交成功")


                },
                error: function(xhr, textStatus, errorThrown) {

                    alert(errorThrown)
                }
            }

            $("#form_rate").ajaxSubmit(params);


        })

        $("#btn_reload").on('click', function() {
            //addItems()
            // displayResult()
           exampleTable.ajax.reload(null,true); // reload
        })

    });

</script>



</body>
</html>
