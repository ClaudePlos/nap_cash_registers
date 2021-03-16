import * as pdfMake from './lib/pdfmake.js';
import * as pdfFonts from './lib/vfs_fonts.js';
pdfMake.vfs = pdfFonts.pdfMake.vfs;

function generateCashReport(container, caseCode, docNumber, period, listDocKpKw) {
    console.log(listDocKpKw);
    let cellValue = JSON.parse(listDocKpKw);

    let bodyData = [];
    bodyData.push(['Lp.', 'Data', 'Numer', 'Treść', 'Przychód', 'Rozchód']);
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
            , '', {text:  quotaMa.toFixed(2), alignment: 'right'}, {text:  quotaWn.toFixed(2), alignment: 'right'}]);
    }
    //summarize
    bodyData.push(['','','','Suma: ', {text:  sumMa.toFixed(2), alignment: 'right'}, {text:  sumWn.toFixed(2), alignment: 'right'}]);
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
                    widths: [20, 'auto', 'auto', 'auto', 100, 100],
                    body: bodyData
                }
            }
        ],
        styles: {
            normal: {
                bold: false,
                fontSize: 15
            },
            mainTitle : {
                bold: true,
                fontSize: 18
            },
            table1: {
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

function test(){
    alert('Hello Test!');
}

window.generateCashReport = generateCashReport;