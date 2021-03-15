import * as pdfMake from './lib/pdfmake.js';
import * as pdfFonts from './lib/vfs_fonts.js';
pdfMake.vfs = pdfFonts.pdfMake.vfs;

function generateCashReport(container, caseCode, docNumber, period, listDocKpKw) {
    console.log(listDocKpKw);
    let cellValue = JSON.parse(listDocKpKw);

    var bodyData = [];
    bodyData.push(['Poz.', 'Data', 'Numer', 'Treść', 'Przychód', 'Rozchód']);

    for (var i = 0; i < cellValue.length; i++) {
        bodyData.push([cellValue[i].docNo
            , ''
            , typeof(cellValue[i].docOwnNumber) != "undefined" ? cellValue[i].docOwnNumber : ''
            , '', cellValue[i].docAmount, cellValue[i].docAmount]);
    }
    console.log(bodyData);

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