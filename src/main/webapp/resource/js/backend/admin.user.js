fblog.register("fblog.user");

fblog.user.remove=function(userid){
 $.ajax({
   type:"DELETE",
   url:fblog.getDomain("users/"+userid),
   dataType:"json",
   success:function(data){
	  if(data&&data.success){
	    window.location.reload();
    }else{
      alert(data.msg);
     }
   }
 });
}

fblog.user.edit=function(userid){
  window.location.href=fblog.getDomain("users/edit?uid="+userid);
}