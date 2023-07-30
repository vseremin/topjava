const userAjaxUrl = "admin/users/";

const ctx = {
    ajaxUrl: userAjaxUrl
};

$(function () {
    makeEditable(
        $("#datatable").DataTable({
            "paging": false,
            "info": true,
            "columns": [
                {
                    "data": "name"
                },
                {
                    "data": "email"
                },
                {
                    "data": "roles"
                },
                {
                    "data": "enabled"
                },
                {
                    "data": "registered"
                },
                {
                    "defaultContent": "Edit",
                    "orderable": false
                },
                {
                    "defaultContent": "Delete",
                    "orderable": false
                }
            ],
            "order": [
                [
                    0,
                    "asc"
                ]
            ]
        })
    );
});

function deleteRow(id) {
    var func = $.ajax({
        url: ctx.ajaxUrl + id,
        type: "DELETE"
    });
    deleteWithFunc(func, userAjaxUrl);
}

function saveUsers() {
     var func = $.ajax({
         type: "POST",
         url: ctx.ajaxUrl,
         data: JSON.stringify({
             name: $("#name").val(),
             email: $("#email").val(),
             password: $("#password").val()
         }),
         contentType: "application/json",
         })
    save(func, ctx.ajaxUrl);
}