const mealsAjaxUrl = "profile/meals/";

const ctx = {
    ajaxUrl: mealsAjaxUrl,
    updateTable: function () {
        $.ajax({
            type: "GET",
            url: ctx.ajaxUrl + "filter",
            data: $("#filter").serialize()
        }).done(function (data) {
            drawTable(data);
        });
    }
};

$(function () {
    makeEditable(
        $("#datatableMeals").DataTable({
            "paging": false,
            "info": true,
            "columns": [
                {
                    "data": "dateTime"
                },
                {
                    "data": "description"
                },
                {
                    "data": "calories"
                },
                {
                    "defaultContent": "update",
                    "orderable": false
                },
                {
                    "defaultContent": "delete",
                    "orderable": false
                }
            ],
            "order": [
                [
                    0,
                    "desc"
                ]
            ]
        })
    );
});

function clearFilter() {
    $("#filter")[0].reset();
    updateTable();
}