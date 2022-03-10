
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
    let title = '送出成功';
    let html = '返回搜尋列表';
    let icon = 'success';
    let redirectPage = false;
    if($('#_redirectPageFlag')){ if( $('#_redirectPageFlag').val() == 'true' ) { redirectPage = true; } }
    if(data){
        if(data.responseMessage){
            html = data.responseMessage;
        }
    }
    //多筆格式的回覆
    if(data.results) {
        html = '此次執行共<i class="text-primary">'+data.total+'</i>筆';
        if(data.success == 0){
            icon = 'error';
            title = '送出失敗';
        }else{
            html += '<br>成功<i class="text-primary">'+data.success+'</i>筆';
        }

        if(data.success != 0 && data.failed != 0){
            icon = 'question';
            title = '送出部分成功';
        }
        if(data.failed != 0){
            html += '<br>失敗<i class="text-danger">'+data.failed+'</i>筆';
        }

        if(data.results.length == 1){
            //單筆
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
            //多筆
            let keys = tableConfig.keys;
            if(keys) {
                //有設定Key才找回該筆
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
                                //抓原始row number
                                let index = dataTable.rows({ filter : 'applied'})[0][i];
                                if(datas.recordStatus == 'NOK'){
                                    $(dataTable.row(index).node()).addClass('table-danger');
                                    let message = datas.recordMessage;
                                    if(message == undefined || message == null || message == "") {
                                        message = "未知錯誤";
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
                title = '未知狀態';
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
            message = '您沒有權限';
        }else if(code == 404){
            message = '網頁不存在';
        }else if(code == 401){
            message = '請勿多開視窗';
        }else if(code == 0){
            message = 'OOPS, 發生錯誤! 後台無回應';
        }else{
            message = 'OOPS, 發生錯誤! ['+code+']';
        }
    }else{
        message = 'OOPS, 發生錯誤! 後台無回應';
    }

    Swal.fire ({
        icon: "error",
        title: '導頁失敗',
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
                        title: '檢核錯誤',
                        text: message
                    });
                }
            }else{
                Swal.fire ({
                    icon: "error",
                    title: '檢核錯誤',
                    text: message
                });
            }
        } else if (jqXHR['responseMessage']) {
            Swal.fire ({
                icon: "error",
                title: '檢核錯誤',
                text: jqXHR['responseMessage']
            });
        }else{
            Swal.fire ({
                icon: "error",
                title: '檢核類錯誤',
                text: getResponseMessage(jqXHR)
            });
        }
    } else if(jqXHR.status === 500) {
        Swal.fire ({
            icon: "error",
            title: '系統錯誤',
            text: getResponseMessage(jqXHR)
        });
    } else if(jqXHR.status === 503) {
         Swal.fire ({
             icon: "error",
             title: '主機無回應',
             text: "["+jqXHR.responseJSON['responseCode']+"]"+jqXHR.responseJSON['responseMessage']
         });
    } else if(jqXHR.status === 404) {
          Swal.fire ({
              icon: "error",
              title: '資料不存在',
              text: jqXHR.responseJSON['responseMessage']
          });
    } else if(jqXHR.status === 409) {
        Swal.fire ({
            icon: "error",
            title: '新增資料重複',
        });
    } else if(jqXHR.status === 401) {
        Swal.fire ({
            icon: "error",
            title: '您重複登入，請重新登入',
            willClose: () => {
                Swal.hideLoading();
                document.location.href = $('#_loginUrl').val();
            }
        });
    } else if(jqXHR.status === 403) {
        Swal.fire ({
            icon: "error",
            title: '您沒此權限',
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
