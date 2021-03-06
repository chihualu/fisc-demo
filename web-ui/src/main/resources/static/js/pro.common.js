
function ajax(url, method, data, successCallback, errorCallback) {
    $('body').click();
    let reqBody = data;
    if( reqBody == null ){
        reqBody = {};
    }
    if(method != 'GET'){
        reqBody = JSON.stringify(data);
    }else {
        $.each(reqBody, function (k, v) {
            reqBody[k] = v.replace(/\|/g, '%7C');
        });
    }
    $.ajax({
        type: method,
        url: url.replace(/\|/g, '%7C'),
        data: reqBody,
        dataType: "json",
        contentType: "application/json;charset=utf-8",
        beforeSend: function (xhr) {
            onLoading();
            xhr.setRequestHeader("Authorization", 'Bearer ' + $('meta[name=token]').attr("content"));
            xhr.setRequestHeader("stepKey", $('#_hidden_stepKey').val());
        },
        success: successCallback,
        error: errorCallback,
        complete: completeLoading
    });
}
function ajaxNormal(url, method, data, successCallback, errorCallback) {
    $('body').click();
    let reqBody = data;
    if(method != 'GET'){
        reqBody = JSON.stringify(data);
    }
    if( reqBody == null ){
        reqBody = "";
    }
    $.ajax({
        type: method,
        url: url,
        data: reqBody,
        timeout: 10000,
        beforeSend: function (xhr) {
            onLoading();
            xhr.setRequestHeader("Authorization", 'Bearer ' + $('meta[name=token]').attr("content"));
        },
        success: successCallback,
        error: errorCallback,
        complete: completeLoading
    });
}
function onLoading(){
    $('body').click();
    $.each($('.overlay'), function(){
        $(this).removeClass('hidden');
    });
    removeDuplicateModal();
}
function removeDuplicateModal(){
    if($('.modal-backdrop')){
        let size = $('.modal-backdrop').length - 1 ;
        $.each($('.modal-backdrop'), function(idx, item){
            if( size != idx){
                $(item).remove();
            }
        });
    }
}

function completeLoading(){
    $.each($('.overlay'), function(){
        $(this).addClass('hidden');
    });
    removeDuplicateModal();
}

function getFormData($form) {
    var unindexed_array = $form.serializeArray();
    var indexed_array = {};
    $.map(unindexed_array, function (n, i) {
        indexed_array[n['name']] = n['value'];
    });
    return indexed_array;
}
function successDefaultHandler(data, status, xhr){
    let title = '????????????';
    let html = '??????????????????';
    let icon = 'success';
    let redirectPage = false;
    if($('#_redirectPageFlag')){ if( $('#_redirectPageFlag').val() == 'true' ) { redirectPage = true; } }
    if(data){
        if(data.responseMessage){
            html = data.responseMessage;
        }
    }
    //?????????????????????
    if(data.results) {
        html = '???????????????<i class="text-primary">'+data.total+'</i>???';
        if(data.success == 0){
            icon = 'error';
            title = '????????????';
        }else{
            html += '<br>??????<i class="text-primary">'+data.success+'</i>???';
        }

        if(data.success != 0 && data.failed != 0){
            icon = 'question';
            title = '??????????????????';
        }
        if(data.failed != 0){
            html += '<br>??????<i class="text-danger">'+data.failed+'</i>???';
        }

        if(data.results.length == 1){
            //??????
            if(icon == 'success'){
                refreshTable();
            }
            Swal.fire({
                title: title,
                html: data.results[0].recordMessage,
                icon: icon,
                willClose: function() {
                    $('#dtlModal').modal('hide');
                    if(redirectPage) {
                        onLoading();
                        backToQueryPage($('#queryForm'));
                    }
                }
            });
        }else{
            //??????
            let keys = tableConfig.keys;
            if(keys) {
                //?????????Key???????????????
                $.each(data.results, function (idx, datas){
                    $.each(dataTable.rows({ filter : 'applied'}).data(), function (i, d){
                        let flag = true;
                        $.each(keys, function (ii, key){
                            if(datas[key] != d[key]){
                                flag = false;
                            }
                        });
                        if( flag ){
                            if(datas.recordStatus){
                                //?????????row number
                                let index = dataTable.rows({ filter : 'applied'})[0][i];
                                if(datas.recordStatus == 'NOK'){
                                    $(dataTable.row(index).node()).addClass('table-danger');
                                    let message = datas.recordMessage;
                                    if(message == undefined || message == null || message == "") {
                                        message = "????????????";
                                    }
                                    $(dataTable.row(index).node()).tooltip( {title: message} );
                                }else{
                                    $(dataTable.row(index).node()).addClass('table-success');
                                }
                            }
                        }
                    });
                });
            }
            if(icon == 'success'){
                refreshTable();
            }
            if( title == null ){
                title = '????????????';
            }
            Swal.fire({
                title: title,
                html: html,
                icon: icon,
                willClose: function() {
                    if(redirectPage) {
                        onLoading();
                        backToQueryPage($('#queryForm'));
                    }
                }
            });
        }
    } else {
        Swal.fire({
            title: title,
            html: html,
            icon: icon,
            willClose: function() {
                onLoading();
                backToQueryPage($('#queryForm'));
            }
        });
    }

}
function failedPageDefaultHandler(jqXHR) {
    let message = '';
    if(error.responseJSON !== undefined){
        let code = error.responseJSON.status;
        if(code == 403){
            message = '???????????????';
        }else if(code == 404){
            message = '???????????????';
        }else if(code == 401){
            message = '??????????????????';
        }else if(code == 0){
            message = 'OOPS, ????????????! ???????????????';
        }else{
            message = 'OOPS, ????????????! ['+code+']';
        }
    }else{
        message = 'OOPS, ????????????! ???????????????';
    }

    Swal.fire ({
        icon: "error",
        title: '????????????',
        text: message,
        willOpen: () => {
            Swal.hideLoading();
        }
    });

}

function failedDefaultHandler(jqXHR){
    if (jqXHR.status === 400) {
        if(jqXHR.responseJSON && jqXHR.responseJSON['responseMessage']){
            let message = jqXHR.responseJSON['responseMessage'];
            if(message.indexOf(':') > 0 && message.indexOf("Invalid input.") !== -1){ // ESAPI validator
                let field = message.split(':')[0];
                if(field){
                    const $fieldName = $('[name=' + field + ']');
                    if($fieldName){
                        $fieldName.addClass('is-invalid');
                        $fieldName.removeClass('is-valid');
                    }
                    $('.alert').text(message);
                    $('.alert').show();
                } else {
                    Swal.fire ({
                        icon: "error",
                        title: '????????????',
                        text: message
                    });
                }
            }else{
                Swal.fire ({
                    icon: "error",
                    title: '????????????',
                    text: message
                });
            }
        } else if (jqXHR['responseMessage']) {
            Swal.fire ({
                icon: "error",
                title: '????????????',
                text: jqXHR['responseMessage']
            });
        }else{
            Swal.fire ({
                icon: "error",
                title: '???????????????',
                text: getResponseMessage(jqXHR)
            });
        }
    } else if(jqXHR.status === 500) {
        Swal.fire ({
            icon: "error",
            title: '????????????',
            text: getResponseMessage(jqXHR)
        });
    } else if(jqXHR.status === 503) {
         Swal.fire ({
             icon: "error",
             title: '???????????????',
             text: "["+jqXHR.responseJSON['responseCode']+"]"+jqXHR.responseJSON['responseMessage']
         });
    } else if(jqXHR.status === 404) {
          Swal.fire ({
              icon: "error",
              title: '???????????????',
              text: jqXHR.responseJSON['responseMessage']
          });
    } else if(jqXHR.status === 409) {
        Swal.fire ({
            icon: "error",
            title: '??????????????????',
        });
    } else if(jqXHR.status === 401) {
        Swal.fire ({
            icon: "error",
            title: '?????????????????????????????????',
            willClose: () => {
                Swal.hideLoading();
                document.location.href = $('#_loginUrl').val();
            }
        });
    } else if(jqXHR.status === 403) {
        Swal.fire ({
            icon: "error",
            title: '???????????????',
            willClose: () => {
                Swal.hideLoading();
                document.location.href = $('#_indexUrl').val();
            }
        });
    } else {
        Swal.fire ({
            icon: "error",
            title: 'ERROR:'+jqXHR.status,
            willClose: () => {
                Swal.hideLoading();
                document.location.href = $('#_indexUrl').val();
            }
        });
        return;
    }
}

function form_Validity(formId){
    $('.alert').hide();
    if($('#'+formId+'.needs-validation')[0].checkValidity() === false){
        $('#'+formId).addClass('was-validated');
        return false;
    }
    return true;
}

function redirectPage(page, method, body, successCallback){
    onLoading();
    if(method == undefined || method == null){
        method = "GET"
    }
    if(body == undefined || body == null){
        body = null;
    }
    ajaxNormal(page, method, body, function(response, textStatus, xhr){
        $('#dashboard').hide();
        $('#mainBody').empty();
        $('#mainBody').html(response);
        $('.tooltip').remove();
        if(successCallback){
            successCallback(response, textStatus, xhr);
        }
    }, failedPageDefaultHandler);
}

function backToQueryPage($Form){
    redirectPage($Form.attr('action'), $Form.attr('method'), getFormData($Form),
        function (response, textStatus, xhr){
            if(xhr.status == '200'){
                if($('.modal-backdrop').is(':visible')){
                    $('.modal-backdrop').toggle();
                }
                completeLoading();
                $('body').attr('style', 'overflow: auto !important');
            }
        });
}
