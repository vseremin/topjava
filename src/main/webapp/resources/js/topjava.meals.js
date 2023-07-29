const mealsAjaxUrl = "profile/meals/";

const ctx = {
    mealsUrl: mealsAjaxUrl,
    updateTable: function () {
        $.ajax({
            type: "GET",
            url: ctx.mealsUrl + "filter",
            data: $("#filter").serialize()
        }).done(function (data) {
            ctx.datatableApi.clear().rows.add(data).draw();
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
    $.ajax({
        url: ctx.mealsUrl + id,
        type: "DELETE"
    }).done(function () {
        updateTableMeals();
        successNoty("Deleted");
    });
}

function updateTableMeals(data) {
    $.get(ctx.mealsUrl, function (data) {
        ctx.datatableApi.clear().rows.add(data).draw();
    });
}

function add() {
    form.find(":input").val("");
    $("#editRow").modal();
}

function updateTable() {
    $.get(ctx.mealsUrl, function (data) {
        ctx.datatableApi.clear().rows.add(data).draw();
    });
}

function saveMeals() {
    var formData = {
        dateTime: $("#dateTime").val(),
        description: $("#description").val(),
        calories: $("#calories").val()
    };

    $.ajax({
        type: "POST",
        url: ctx.mealsUrl,
        data: JSON.stringify(formData),
        contentType: "application/json",
    }).done(function () {
        $("#editRow").modal("hide");
        updateTable();
        successNoty("Saved");
    });
}
