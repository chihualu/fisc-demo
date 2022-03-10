function idCheck(val) {
    var city = [1, 10, 19, 28, 37, 46, 55, 64, 39, 73, 82, 2, 11, 20, 48, 29, 38, 47, 56, 65, 74, 83, 21, 3, 12, 30];
    var id = val.toUpperCase();

    // 使用「正規表達式」檢驗格式
    if (!id.match(/^[A-Z]\d{9}$/) && !id.match(/^[A-Z][A-D]\d{8}$/)) {
        return '身份證格式錯誤!';
    } else {
        var total = 0;
        if (id.match(/^[A-Z]\d{9}$/)) { //身分證字號
            //將字串分割為陣列(IE必需這麼做才不會出錯)
            id = id.split('');
            //計算總分
            total = city[id[0].charCodeAt(0) - 65];
            for (var i = 1; i <= 8; i++) {
                total += eval(id[i]) * (9 - i);
            }
        } else { // 外來人口統一證號
            //將字串分割為陣列(IE必需這麼做才不會出錯)
            id = id.split('');
            //計算總分
            total = city[id[0].charCodeAt(0) - 65];
            // 外來人口的第2碼為英文A-D(10~13)，這裡把他轉為區碼並取個位數，之後就可以像一般身分證的計算方式一樣了。
            id[1] = id[1].charCodeAt(0) - 65;
            for (var i = 1; i <= 8; i++) {
                total += eval(id[i]) * (9 - i);
            }
        }
        //補上檢查碼(最後一碼)
        total += eval(id[9]);
        //檢查比對碼(餘數應為0);
        if (total % 10 != 0) {
            return '身份證號碼驗證錯誤!';
        }
    }
    return '';
}

function banCheck(id) {
    var invalidList = "00000000,11111111";
    // 統一編號格式
    if (/^\d{8}$/.test(id) == false || invalidList.indexOf(id) != -1) {
        return '統一編號格式錯誤!';
    }

    var validateOperator = [1, 2, 1, 2, 1, 2, 4, 1],
        sum = 0,
        calculate = function (product) { // 個位數 + 十位數
            var ones = product % 10,
                tens = (product - ones) / 10;
            return ones + tens;
        };
    for (var i = 0; i < validateOperator.length; i++) {
        sum += calculate(id[i] * validateOperator[i]);
    }

    if (sum % 10 == 0 || (id[6] == "7" && (sum + 1) % 10 == 0)) {
        return '';
    } else {
        return '統一編號驗證錯誤!';

    }
}

function idnBanCheck(val) {

    if (val.length == 10) {
        var city = [1, 10, 19, 28, 37, 46, 55, 64, 39, 73, 82, 2, 11, 20, 48, 29, 38, 47, 56, 65, 74, 83, 21, 3, 12, 30];
        var id = val.toUpperCase();

        // 使用「正規表達式」檢驗格式
        if (!id.match(/^[A-Z]\d{9}$/) && !id.match(/^[A-Z][A-D]\d{8}$/)) {
            return '身份證格式錯誤!';
        } else {
            var total = 0;
            if (id.match(/^[A-Z]\d{9}$/)) { //身分證字號
                //將字串分割為陣列(IE必需這麼做才不會出錯)
                id = id.split('');
                //計算總分
                total = city[id[0].charCodeAt(0) - 65];
                for (var i = 1; i <= 8; i++) {
                    total += eval(id[i]) * (9 - i);
                }
            } else { // 外來人口統一證號
                //將字串分割為陣列(IE必需這麼做才不會出錯)
                id = id.split('');
                //計算總分
                total = city[id[0].charCodeAt(0) - 65];
                // 外來人口的第2碼為英文A-D(10~13)，這裡把他轉為區碼並取個位數，之後就可以像一般身分證的計算方式一樣了。
                id[1] = id[1].charCodeAt(0) - 65;
                for (var i = 1; i <= 8; i++) {
                    total += eval(id[i]) * (9 - i);
                }
            }
            //補上檢查碼(最後一碼)
            total += eval(id[9]);
            //檢查比對碼(餘數應為0);
            if (total % 10 != 0) {
                return '身份證號碼驗證錯誤!';
            }
        }
        return '';
    } else if (val.length == 8) {
        var invalidList = "00000000,11111111";
        // 統一編號格式
        if (/^\d{8}$/.test(val) == false || invalidList.indexOf(val) != -1) {
            return '統一編號格式錯誤!';
        }

        var validateOperator = [1, 2, 1, 2, 1, 2, 4, 1],
            sum = 0,
            calculate = function (product) { // 個位數 + 十位數
                var ones = product % 10,
                    tens = (product - ones) / 10;
                return ones + tens;
            };
        for (var i = 0; i < validateOperator.length; i++) {
            sum += calculate(val[i] * validateOperator[i]);
        }

        if (sum % 10 == 0 || (val[6] == "7" && (sum + 1) % 10 == 0)) {
            return '';
        } else {
            return '統一編號驗證錯誤!';

        }
    } else {
        return '統編/身份證字號格式錯誤!';
    }
}

function bankCodeCheck(val) {
    // 使用「正規表達式」檢驗格式
    if (!val.match(/^\d{7,}$/)) {
        return '總行+分行代號格式錯誤!';
    }
    return '';
}

function mainCodeCheck(val) {
    // 使用「正規表達式」檢驗格式
    if (!val.match(/^\d{3,}$/)) {
        return '總行代號格式錯誤!';
    }
    return '';
}

function acctCodeCheck(val) {
    // 使用「正規表達式」檢驗格式
    if (!val.match(/^\d{9,}$/)) {
        return '開戶行代號格式錯誤!';
    }
    return '';
}

function acctNoCheck(val) {
    // 使用「正規表達式」檢驗格式
    if (!val.match(/^[0-9]*$/)) {
        return '開戶行帳號格式錯誤!';
    }
    return '';
}


/**
 * 日期解析，字符串轉日期
 * @param dateString 可以為2017-02-16，2017/02/16，2017.02.16
 * @returns {Date} 返回對應的日期對象
 */
function dateParse(dateString) {
    var SEPARATOR_BAR = "-";
    var SEPARATOR_SLASH = "/";
    var SEPARATOR_DOT = ".";
    var dateArray;
    if (dateString.indexOf(SEPARATOR_BAR) > -1) {
        dateArray = dateString.split(SEPARATOR_BAR);
    } else if (dateString.indexOf(SEPARATOR_SLASH) > -1) {
        dateArray = dateString.split(SEPARATOR_SLASH);
    } else {
        dateArray = dateString.split(SEPARATOR_DOT);
    }
    return new Date(dateArray[0], dateArray[1] - 1, dateArray[2]);
}

/**
 * 日期比較大小
 * compareDateString大於dateString，返回1；
 * 等於返回0；
 * compareDateString小於dateString，返回-1
 * @param dateString 日期
 * @param compareDateString 比較的日期
 */
function dateCompare(dateString, compareDateString) {
    var dateTime = dateParse(dateString).getTime();
    var compareDateTime = dateParse(compareDateString).getTime();
    if (compareDateTime > dateTime) {
        return 1;
    } else if (compareDateTime == dateTime) {
        return 0;
    } else {
        return -1;
    }
}