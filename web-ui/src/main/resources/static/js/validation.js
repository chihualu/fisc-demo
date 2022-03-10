$(function () {

    $('#contentBody form[method=POST]').each(function () {
        $(this).submit(function () {
            console.log('找SA謝謝');
            return false;
        })
    });

    function checkPattern(obj) {
        $(obj).on('change', function () {
            if ($(obj)[0].checkValidity() === false) {
                $(obj).addClass('is-invalid');
                $(obj).removeClass('is-valid');
            } else {
                $(obj).removeClass('is-invalid');
                $(obj).addClass('is-valid');
            }
        });
    }

    $('form.needs-validation').each(function () {
        $(this).find(':input').each(function () {
            checkPattern(this);
        });
        $(this).find(':radio').each(function () {
            checkPattern(this);
        });
        $(this).find(':checkbox').each(function () {
            checkPattern(this);
        });
    })
});



