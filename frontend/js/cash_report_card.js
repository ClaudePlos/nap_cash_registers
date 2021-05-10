import * as pdfMake from './lib/pdfmake.js';
import * as pdfFonts from './lib/vfs_fonts.js';
pdfMake.vfs = pdfFonts.pdfMake.vfs;

function generateCashReportCard(container, caseCode, docNumber, period, listDocKpKw, valueInitialState) {
    console.log(listDocKpKw);
    let cellValue = JSON.parse(listDocKpKw);

    let bodyData = [];
    bodyData.push(['Lp', 'Data', 'Numer', 'Treść', 'Dod. Info.', 'Przychód', 'Rozchód']);
    let sumMa = 0;
    let sumWn = 0;

    for (var i = 0; i < cellValue.length; i++) {
        let quotaMa = 0;
        let quotaWn = 0;
        if (cellValue[i].docRdocCode === 'KP'){
            quotaMa = cellValue[i].docAmount;
            sumMa += cellValue[i].docAmount;
        } else {
            quotaWn = cellValue[i].docAmount;
            sumWn += cellValue[i].docAmount;
        }

        let year = cellValue[i].docDateFrom.year;
        let month = "0"+cellValue[i].docDateFrom.month;
        let day = "0"+cellValue[i].docDateFrom.day;


        bodyData.push([cellValue[i].docNo
            ,  year + "-" + month.substr(month.length-2, month.length) + "-" + day.substr(day.length-2, day.length)
            , typeof(cellValue[i].docOwnNumber) != "undefined" ? cellValue[i].docOwnNumber : ''
            , typeof(cellValue[i].docDescription) != "undefined" ? cellValue[i].docDescription : ''
            , getAdditionalInfo(cellValue[i])
            , {text:  quotaMa.toFixed(2), alignment: 'right'}, {text:  quotaWn.toFixed(2), alignment: 'right'}]);
    }
    //summarize
    let lValueInitialState = Number(valueInitialState);
    let allMa = sumMa + lValueInitialState;
    let lValueEndState = (sumMa - sumWn) + lValueInitialState;
    let allWn = sumWn + lValueEndState;
    bodyData.push(['','','','',{text:  'Razem obroty:', alignment: 'right'}, {text:  sumMa.toFixed(2), alignment: 'right', fillColor: 'grey'}
        , {text:  sumWn.toFixed(2), alignment: 'right', fillColor: 'grey',}]);
    bodyData.push(['','','','',{text:  'Stan Kasy poprzedni:', alignment: 'right'},{text:  lValueInitialState.toFixed(2), alignment: 'right'}, '']);
    bodyData.push(['','','','',{text:  'Stan Kasy obecny:', alignment: 'right'}, '', {text:  lValueEndState.toFixed(2), alignment: 'right'}]);
    bodyData.push(['','','','',{text:  'Razem:', alignment: 'right'}, {text:  allMa.toFixed(2), alignment: 'right'}, {text:  allWn.toFixed(2), alignment: 'right'}]);
    //console.log(bodyData);

    var docDefinition = {
        content: [
            { text: 'Raport Kasowy nr: ' + docNumber, style: 'mainTitle', alignment: "center" },
            ' ',
            { text: 'Za okres: ' + period, style: 'normal'},
            { text: 'Kasa: ' + caseCode, style: 'normal'},
            ' ',
            {
                style: 'table1',
                table: {
                    widths: [10, 42, 85, 130, 70, 60, 60],
                    body: bodyData
                }
            }
        ],
        styles: {
            normal: {
                bold: false,
                fontSize: 12
            },
            mainTitle : {
                bold: true,
                fontSize: 18
            },
            table1: {
                fontSize: 8,
                margin: [0, 5, 0, 15]
            },
        },
        defaultStyle: {
            fontSize: 12
        }
    };

    //const docDefinition = doc(tableBody);
    pdfMake.createPdf(docDefinition).open();
    // pdfMake.createPdf(docDefinition).download('file.pdf', function () {
    //     alert('Plik PDF został wygenerowany');
    // });
}


function getAdditionalInfo( item ) {
    let ret = '';

    if (typeof(item.docPrcIdPod) != "undefined") {
        ret = item.fullNameForPrcIdPod;
    }

    if (typeof(item.docKlKodPod) != "undefined") {
        ret = item.docKlKodPod;
    }

    if (typeof(item.docDef2) != "undefined") {
        ret = item.docDef2;
    }

    return ret;
}

window.generateCashReportCard = generateCashReportCard;