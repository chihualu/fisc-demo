var queryReasonRule = '';
var byte1Value = '';
var byte2Value = 0;
var byte2ValueArray = [];
var byte3Value = '';
var isSpecialBank = true;

$("[data-byte1]").click(function () {
    //byte1Value = $(this).data("byte1");
    if (queryReasonRule == 'ANY') {
        if ($(this).data("byte1") == "D" || $(this).data("byte1") == "E") {
            $("[data-byte2]").prop('disabled', 'disabled');
            $("[data-byte3]").prop('disabled', 'disabled');
            $("[data-byte2]").prop('checked', false);
            $("[data-byte3]").prop('checked', false);
        } else if ($(this).data("byte1") == "C") {
            $("[data-byte2]").prop('disabled', '');

            $("[data-byte3]").prop('disabled', 'disabled');
            $("[data-byte3]").prop('checked', false);
        } else {
            $("[data-byte2]").prop('disabled', '');
            $("[data-byte3]").prop('disabled', '');
        }
    }
});

$("[data-byte2]").click(function () {
//	var isChecked = $(this).is(':checked');
//	var inputValue = $(this).data("byte2");
//	
//	if (isChecked) {
//		byte2Value += $(this).data("byte2value") * 1;
//		byte2ValueArray.push(inputValue);
//	} else {
//		byte2Value -= $(this).data("byte2value") * 1;
//		byte2ValueArray = byte2ValueArray.filter(function(arrValue) {
//			return arrValue != inputValue;
//		});
//	}

});

$("[data-byte3]").click(function () {
//	byte3Value = $(this).data("byte3");
});

function getQueryReasonValue() {
    byte1Value = 0;
    byte2ValueArray = [];
    byte2Value = 0;
    byte3Value = 0;
    $("[data-byte1]").each(function () {
        if ($(this).is(':checked')) {
            byte1Value = $(this).data("byte1");
        }
    });

    $("[data-byte2]").each(function () {
        if ($(this).is(':checked')) {
            var inputValue = $(this).data("byte2");
            byte2Value += $(this).data("byte2value") * 1;
            byte2ValueArray.push(inputValue);
        }
    });

    $("[data-byte3]").each(function () {
        if ($(this).is(':checked')) {
            byte3Value = $(this).data("byte3");
        }
    });
}

function commonCheck() {
    getQueryReasonValue();

    var queryReasonArray = queryReasonRule.split('_');
    var queryReason = '';

    if (byte1Value == 0) {
        //alert('查詢理由「第一層」 必單選!</br>');
        Swal.fire({
            icon: 'error',
            text: '查詢理由「第一層」 必單選!',
            showCloseButton: true,
            buttonsStyling: false,
            customClass: {
                confirmButton: 'btn btn-primary mx-2 px-4',
                cancelButton: 'btn btn-danger mx-2 px-4',
            },
            confirmButtonText: '確認',
        })
        return false;
    }

    // D.公開/公示資訊、E.統計類資訊 不須點選第二層及第三層選項。
    if (byte1Value == "D" || byte1Value == "E") {
        byte2Value = 0;
        byte3Value = 0;
        queryReason = byte1Value + byte2Value + byte3Value;
        $('#queryReason').val(queryReason);
        //return confirm('須確認同意書載有當事人(買方)同意被查詢其信用資訊及買方為授信對象報送各項資料。');
        return true;
    }

    if (byte2Value == 0) {
        //alert("查詢理由「第二層」至少選擇一選項");
        Swal.fire({
            icon: 'error',
            text: '查詢理由「第二層」至少選擇一選項',
            showCloseButton: true,
            buttonsStyling: false,
            customClass: {
                confirmButton: 'btn btn-primary mx-2 px-4',
                cancelButton: 'btn btn-danger mx-2 px-4',
            },
            confirmButtonText: '確認',
        })
        return false;
    }

    if (queryReasonArray.length > 1) {
        var byte1Rule = queryReasonArray[0].split('-');
        var byte2Rule = queryReasonArray[1].split('-');
        var byte3Rule = queryReasonArray[2].split('-');

        var isCheck = true;
        var msg = '';

        if (byte1Rule[0] != 'ANY' && byte1Rule[0] != 0) {
            if (byte1Rule.indexOf(byte1Value) == -1) {
                var byte1Msg = byte1Content.filter(function (item, index) {
                    return byte1Rule.indexOf(item.type) > -1;
                });
                byte1Msg = byte1Msg.map(function (item, index) {
                    return item.name;
                });
                byte1Msg = byte1Msg.join("、")
                msg += '第一層只可選「' + byte1Msg + '」</br>';
                isCheck = false;
            }
        }

        if (byte2Rule[0] != 'ANY' && byte2Rule[0] != 0) {
            var isWrong = false;
            byte2ValueArray.forEach(function (value) {
                if (byte2Rule.indexOf(value) == -1) {
                    isWrong = true;
                }
            })

            if (isWrong) {
                var byte2Msg = byte2Content.filter(function (item, index) {
                    return byte2Rule.indexOf(item.type) > -1;
                });
                byte2Msg = byte2Msg.map(function (item, index) {
                    return item.name;
                });
                byte2Msg = byte2Msg.join("、")
                msg += '第二層只可選「' + byte2Msg + '」</br>';
                isCheck = false;
            }
        }

        if (byte3Rule[0] != 'ANY' && byte3Rule[0] != 0) {
            if (byte3Value == '') {
                msg += '查詢理由「第三層」 必單選!</br>';
                isCheck = false;
            } else if (byte3Rule.indexOf(byte3Value) == -1) {
                var byte3Msg = byte3Content.filter(function (item, index) {
                    return byte3Rule.indexOf(item.type) > -1;
                });
                byte3Msg = byte3Msg.map(function (item, index) {
                    return item.name;
                });
                byte3Msg = byte3Msg.join("、")
                msg += '第三層只可選「' + byte3Msg + '」</br>';
                isCheck = false;
            }
        }

        if (!isCheck) {
            Swal.fire({
                icon: 'error',
                html: msg,
                showCloseButton: true,
                buttonsStyling: false,
                customClass: {
                    confirmButton: 'btn btn-primary mx-2 px-4',
                    cancelButton: 'btn btn-danger mx-2 px-4',
                },
                confirmButtonText: '確認',
            })
            return false;
        }
    }

    if ($('[data-byte2="X"]').is(':checked')) {
        if (byte2Value > 33) {
//			alert("存款開戶限單選！");
            Swal.fire({
                icon: 'error',
                text: '存款開戶限單選！',
                showCloseButton: true,
                buttonsStyling: false,
                customClass: {
                    confirmButton: 'btn btn-primary mx-2 px-4',
                    cancelButton: 'btn btn-danger mx-2 px-4',
                },
                confirmButtonText: '確認',
            })
            return false;
        }
    }

    if ($('[data-byte2="Z"]').is(':checked')) {
        if (byte2Value > 35) {
//			alert("衍生性金融商品業務限單選！");
            Swal.fire({
                icon: 'error',
                text: '衍生性金融商品業務限單選！',
                showCloseButton: true,
                buttonsStyling: false,
                customClass: {
                    confirmButton: 'btn btn-primary mx-2 px-4',
                    cancelButton: 'btn btn-danger mx-2 px-4',
                },
                confirmButtonText: '確認',
            })
            return false;
        } else {
            if (byte3Value != 'A') {
//				alert('第三層須續點選「取得當事人同意」');
                Swal.fire({
                    icon: 'error',
                    text: '第三層須續點選「取得當事人同意」',
                    showCloseButton: true,
                    buttonsStyling: false,
                    customClass: {
                        confirmButton: 'btn btn-primary mx-2 px-4',
                        cancelButton: 'btn btn-danger mx-2 px-4',
                    },
                    confirmButtonText: '確認',
                })
                return false;
            }
        }
    }

    if ($('[data-byte2="E"]').is(':checked')) {
        if (byte1Value != 'X') {
            Swal.fire({
                icon: 'error',
                text: '第二層點選「其他」，第一層只能點選「其他」！',
                showCloseButton: true,
                buttonsStyling: false,
                customClass: {
                    confirmButton: 'btn btn-primary mx-2 px-4',
                    cancelButton: 'btn btn-danger mx-2 px-4',
                },
                confirmButtonText: '確認',
            })
            return false;
        }
        if (byte3Value == 0) {
//			alert("第二層點選「其他」，須續點選第三層！");
            Swal.fire({
                icon: 'error',
                text: '第二層點選「其他」，須續點選第三層！',
                showCloseButton: true,
                buttonsStyling: false,
                customClass: {
                    confirmButton: 'btn btn-primary mx-2 px-4',
                    cancelButton: 'btn btn-danger mx-2 px-4',
                },
                confirmButtonText: '確認',
            })
            return false;
        }
    }


    // 帳戶管理只有特約銀行才可以選
    if (byte1Value == "C") {
        if (isSpecialBank) {
            if (byte2Value != 1 && byte2Value != 3) {
//				alert("第一層為「帳戶管理」時，第二層必須勾選「信用卡業務、現金卡業務」或只勾選「信用卡業務」");
                Swal.fire({
                    icon: 'error',
                    text: '第一層為「帳戶管理」時，第二層必須勾選「信用卡業務、現金卡業務」或只勾選「信用卡業務」',
                    showCloseButton: true,
                    buttonsStyling: false,
                    customClass: {
                        confirmButton: 'btn btn-primary mx-2 px-4',
                        cancelButton: 'btn btn-danger mx-2 px-4',
                    },
                    confirmButtonText: '確認',
                })
                return false;
            }
            byte3Value = 0;
        } else {
//			alert("非特約銀行第一層無法勾選「帳戶管理」");
            Swal.fire({
                icon: 'error',
                text: '非特約銀行第一層無法勾選「帳戶管理」',
                showCloseButton: true,
                buttonsStyling: false,
                customClass: {
                    confirmButton: 'btn btn-primary mx-2 px-4',
                    cancelButton: 'btn btn-danger mx-2 px-4',
                },
                confirmButtonText: '確認',
            })
            return false;
        }
    } else if ("X" == byte1Value && byte2Value != 16) {
//		alert('當「第一層」為「其他」時，「第二層」只能選「其他');
        Swal.fire({
            icon: 'error',
            text: '當「第一層」為「其他」時，「第二層」只能選「其他」',
            showCloseButton: true,
            buttonsStyling: false,
            customClass: {
                confirmButton: 'btn btn-primary mx-2 px-4',
                cancelButton: 'btn btn-danger mx-2 px-4',
            },
            confirmButtonText: '確認',
        })
        return false;
    }

    if (byte2Value > 9) {
        if (byte2Value + 55 < 128) {
            byte2Value = String.fromCharCode(byte2Value + 55);
        } else {
//			alert("勾選查詢理由選項二有誤，請再次確認！");
            Swal.fire({
                icon: 'error',
                text: '勾選查詢理由選項二有誤，請再次確認！',
                showCloseButton: true,
                buttonsStyling: false,
                customClass: {
                    confirmButton: 'btn btn-primary mx-2 px-4',
                    cancelButton: 'btn btn-danger mx-2 px-4',
                },
                confirmButtonText: '確認',
            })
            return false;
        }
    }

    if ($("[data-byte3]").attr('disabled') !== 'disabled' && byte3Value == 0) {
        Swal.fire({
            icon: 'error',
            text: '查詢理由「第三層」 必單選!',
            showCloseButton: true,
            buttonsStyling: false,
            customClass: {
                confirmButton: 'btn btn-primary mx-2 px-4',
                cancelButton: 'btn btn-danger mx-2 px-4',
            },
            confirmButtonText: '確認',
        })
        return false;
    }

    queryReason = byte1Value + byte2Value + byte3Value;
    $('#queryReason').val(queryReason);

//	if (!confirm('須確認同意書載有當事人(買方)同意被查詢其信用資訊及買方為授信對象報送各項資料。')) {
//		if (!Number.isInteger(byte2Value)) {
//			byte2Value = byte2Value.charCodeAt(0) - 55;
//		}
//		return false;
//	} 
    return true;
}

function queryReasonCheck() {
    getQueryReasonValue();

    var queryReasonArray = queryReasonRule.split('_');
    var byte1Rule = queryReasonArray[0].split('-');
    var byte2Rule = queryReasonArray[1].split('-');
    var byte3Rule = queryReasonArray[2].split('-');

    var isCheck = true;
    var msg = '';


    if (queryReasonRule != '') {

        if (byte1Rule[0] != 'ANY' && byte1Rule[0] != 0) {
            if (byte1Value == '') {
                msg += '查詢理由「第一層」 必單選!</br>';
                isCheck = false;
            } else if (byte1Rule.indexOf(byte1Value) == -1) {
                var byte1Msg = byte1Content.filter(function (item, index) {
                    return byte1Rule.indexOf(item.type) > -1;
                });
                byte1Msg = byte1Msg.map(function (item, index) {
                    return item.name;
                });
                byte1Msg = byte1Msg.join("、")
                msg += '第一層只可選「' + byte1Msg + '」</br>';
                isCheck = false;
            }
        }

        if (byte2Rule[0] != 'ANY' && byte2Rule[0] != 0) {
            var isWrong = false;
            byte2ValueArray.forEach(function (value) {
                if (byte2Rule.indexOf(value) == -1) {
                    isWrong = true;
                }
            })

            if (isWrong) {
                var byte2Msg = byte2Content.filter(function (item, index) {
                    return byte2Rule.indexOf(item.type) > -1;
                });
                byte2Msg = byte2Msg.map(function (item, index) {
                    return item.name;
                });
                byte2Msg = byte2Msg.join("、")
                msg += '第二層只可選「' + byte2Msg + '」</br>';
                isCheck = false;
            } else if (byte2Value == 0) {
                msg += '查詢理由「第二層」至少選擇一選項</br>';
                isCheck = false;
            } else if ("X" == byte1Value && byte2Value != 16) {
                msg += '當「第一層」為「其他」時，「第二層」只能選「其他」</br>';
                isCheck = false;
            }

            if ($('[data-byte2="E"]').is(':checked')) {
                if ("X" != byte1Value) {
                    msg += '第二層點選「其他」，第一層只能點選「其他」</br>';
                    isCheck = false;
                }
            }

        }

        if (byte3Rule[0] != 'ANY' && byte3Rule[0] != 0) {
            if (byte3Value == '') {
                msg += '查詢理由「第三層」 必單選!</br>';
                isCheck = false;
            } else if (byte3Rule.indexOf(byte3Value) == -1) {
                var byte3Msg = byte3Content.filter(function (item, index) {
                    return byte3Rule.indexOf(item.type) > -1;
                });
                byte3Msg = byte3Msg.map(function (item, index) {
                    return item.name;
                });
                byte3Msg = byte3Msg.join("、")
                msg += '第三層只可選「' + byte3Msg + '」</br>';
                isCheck = false;
            }
        }

        if (!isCheck) {
//			alert(msg);
            Swal.fire({
                icon: 'error',
                html: msg,
                showCloseButton: true,
                buttonsStyling: false,
                customClass: {
                    confirmButton: 'btn btn-primary mx-2 px-4',
                    cancelButton: 'btn btn-danger mx-2 px-4',
                },
                confirmButtonText: '確認',
            })
        } else {
            if (byte2Value > 9) {
                if (byte2Value + 55 < 128) {
                    byte2Value = String.fromCharCode(byte2Value + 55);
                } else {
//					alert("勾選查詢理由選項二有誤，請再次確認！");
                    Swal.fire({
                        icon: 'error',
                        text: '勾選查詢理由選項二有誤，請再次確認！',
                        showCloseButton: true,
                        buttonsStyling: false,
                        customClass: {
                            confirmButton: 'btn btn-primary mx-2 px-4',
                            cancelButton: 'btn btn-danger mx-2 px-4',
                        },
                        confirmButtonText: '確認',
                    })
                    return false;
                }
            }

            var queryReason = byte1Value + byte2Value + byte3Value;
            $('#queryReason').val(queryReason);
        }

//		if (isCheck) {
//			if (!confirm('須確認同意書載有當事人(買方)同意被查詢其信用資訊及買方為授信對象報送各項資料。')) {
//				if (!Number.isInteger(byte2Value)) {
//					byte2Value = byte2Value.charCodeAt(0) - 55;
//				}
//				return false;
//			}
//			return true;
//		} else {
//			return isCheck;
//		}
        return isCheck;

    }
}

function initQueryReason() {

    var byte1Msg = [];
    var byte2Msg = [];
    var byte3Msg = [];

    if (queryReasonRule != 'ANY') {
        var queryReasonArray = queryReasonRule.split('_');
        var byte1Rule = queryReasonArray[0].split('-');
        var byte2Rule = queryReasonArray[1].split('-');
        var byte3Rule = queryReasonArray[2].split('-');

        if (queryReasonArray[0] == '0') {
            $("[data-byte1]").prop('disabled', 'disabled');
            $('#byte1Desc').text('(不需選擇)');
            byte1Value = '0';
            byte1Msg.push('不需選擇')
        } else {
            if (byte1Rule != 'ANY') {
                byte1Msg = byte1Content.filter(function (item, index) {
                    return byte1Rule.indexOf(item.type) > -1;
                });
                byte1Msg = byte1Msg.map(function (item, index) {
                    return item.name;
                });
                byte1Msg = byte1Msg.join("、")
            } else {
                byte1Msg = '依查詢理由規則';
            }

        }


        if (queryReasonArray[1] == '0') {
            $("[data-byte2]").prop('disabled', 'disabled');
            $('#byte2Desc').text('(不需選擇)');
            byte2Value = '0';
            byte2Msg.push('不需選擇')
        } else {
            if (byte2Rule != 'ANY') {
                byte2Msg = byte2Content.filter(function (item, index) {
                    return byte2Rule.indexOf(item.type) > -1;
                });
                byte2Msg = byte2Msg.map(function (item, index) {
                    return item.name;
                });
                byte2Msg = byte2Msg.join("、")
            } else {
                byte2Msg = '依查詢理由規則';
            }
        }

        if (queryReasonArray[2] == '0') {
            $("[data-byte3]").prop('disabled', 'disabled');
            $('#byte3Desc').text('(不需選擇)');
            byte3Value = '0';
            byte3Msg.push('不需選擇')
        } else {
            if (byte3Rule != 'ANY') {
                byte3Msg = byte3Content.filter(function (item, index) {
                    return byte3Rule.indexOf(item.type) > -1;
                });
                byte3Msg = byte3Msg.map(function (item, index) {
                    return item.name;
                });
                byte3Msg = byte3Msg.join("、")
            } else {
                byte3Msg = '依查詢理由規則';
            }
        }
        var msg = '';
        msg += '<div style="text-align: left;"><span>第一層：「' + byte1Msg + '」</span></div>';
        msg += '<div style="text-align: left;"><span>第二層：「' + byte2Msg + '」</span></div>';
        msg += '<div style="text-align: left;"><span>第三層：「' + byte3Msg + '」</span></div>';

        $('#queryReasonInfo').tooltip('dispose');
        $('#queryReasonInfo').attr('title', msg);
        $('#queryReasonInfo').tooltip({container: 'body'});
    } else {
        var msg = '依共用查詢理由規則';
        $("[data-byte1]").prop('disabled', '');
        $("[data-byte2]").prop('disabled', '');
        $("[data-byte3]").prop('disabled', '');
        $('#byte1Desc').text('');
        $('#byte2Desc').text('');
        $('#byte3Desc').text('');

        $('#queryReasonInfo').tooltip('dispose');
        $('#queryReasonInfo').attr('title', msg);
        $('#queryReasonInfo').tooltip({container: 'body'});
    }

}

  
