const mealsAjaxUrl = "profile/meals/";

const ctx = {
    mealsUrl: mealsAjaxUrl,
    updateTable: function () {
        $.ajax({
            type: "GET",
            url: ctx.mealsUrl + "filter",
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

function deleteRow(id) {
    var func = $.ajax({
        url: ctx.mealsUrl + id,
        type: "DELETE"
    });
    deleteWithFunc(func, ctx.mealsUrl);
}

function saveMeals() {
    var func = $.ajax({
            type: "POST",
            url: ctx.mealsUrl,
            data: JSON.stringify({
                dateTime: $("#dateTime").val(),
                description: $("#description").val(),
                calories: $("#calories").val()
            }),
            contentType: "application/json",
        })
    save(func, ctx.mealsUrl);
}

