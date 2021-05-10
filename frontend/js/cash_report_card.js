import * as pdfMake from './lib/pdfmake.js';
import * as pdfFonts from './lib/vfs_fonts.js';
pdfMake.vfs = pdfFonts.pdfMake.vfs;

function generateCashReportCard(container, caseCode, docNumber, period, listDocKpKw, valueInitialState) {
    console.log("test dogie");
}

window.generateCashReportCard = generateCashReportCard;