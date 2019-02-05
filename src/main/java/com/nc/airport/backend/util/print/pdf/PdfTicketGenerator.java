package com.nc.airport.backend.util.print.pdf;


import com.itextpdf.text.*;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;
import com.nc.airport.backend.model.dto.PrintableTicket;
import com.nc.airport.backend.util.exceptions.PrintException;
import lombok.extern.log4j.Log4j2;
import org.springframework.core.io.ClassPathResource;

import java.io.IOException;
import java.io.OutputStream;
import java.time.temporal.ChronoUnit;
import java.util.Arrays;
import java.util.function.Consumer;

@Log4j2
public class PdfTicketGenerator {
    private final static Font CELLS_CONTENT_TITLES = new Font(Font.FontFamily.HELVETICA, 8, Font.UNDERLINE);
    private final static Font CELLS_CONTENT_FIELDS = new Font(Font.FontFamily.COURIER, 8, Font.NORMAL);

    public static void putTicketPdfInOutputStream(PrintableTicket ticket, OutputStream outputStream) {
        Rectangle rectangle = new Rectangle(PageSize.A6.rotate());
        Document document = new Document(rectangle);
        document.setMargins(0, 0, 0, 0);

        try {
            PdfWriter.getInstance(document, outputStream);
            document.open();
            fillMetaData(document, ticket);

            PdfPTable rootTable = new PdfPTable(new float[] {1, 0.6f});
            PdfPTable baseTable = new PdfPTable(1);
            PdfPTable sideTable = new PdfPTable(1);
            rootTable.setWidthPercentage(100);
            baseTable.setWidthPercentage(100);
            sideTable.setWidthPercentage(100);

            baseTable.addCell(writeBaseHeader(ticket));
            sideTable.addCell(writeSideHeader(ticket));

            String passengerName = ticket.getPassengerFirstName() + " " + ticket.getPassengerLastName();
            baseTable.addCell(writeInBase(
                    "Name of passenger",
                    passengerName
            ));
            sideTable.addCell(writeInSide(
                    "Name of passenger",
                    passengerName
            ));

            baseTable.addCell(writeInBase(
                    "From",
                    "Date",
                    "Time",
                    ticket.getDepartureCity(),
                    ticket.getExpectedDepartureDatetime().toLocalDate().toString(),
                    ticket.getExpectedDepartureDatetime().toLocalTime().truncatedTo(ChronoUnit.MINUTES).toString()
            ));
            sideTable.addCell(writeInSide(
                    "From",
                    ticket.getDepartureCity()
            ));

            baseTable.addCell(writeInBase(
                    "To",
                    "Flight",
                    "Class",
                    ticket.getArrivalCity(),
                    ticket.getFlightNumber(),
                    ticket.getSeatType().toUpperCase()
            ));
            sideTable.addCell(writeInSide(
                    "To",
                    ticket.getArrivalCity()
            ));

            int gateNum = Integer.valueOf(ticket.getFlightNumber().replaceAll("\\D+","")) % 10;
            String gate = gateNum == 0 ? "10" : "0" + gateNum;
            String boardingTill = ticket.getExpectedArrivalDatetime().toLocalTime().truncatedTo(ChronoUnit.MINUTES).minusMinutes(30).toString();
            String seat = "" + ticket.getSeatRow() + ((char) (ticket.getSeatRow() + 64));
            baseTable.addCell(writeInStyle(
                    PdfTicketGenerator::setToLastBaseLineContentWrapperStyle,
                    new float[] {1, 1, 1},
                    "Gate",
                    "Boarding till",
                    "Seat",
                    gate,
                    boardingTill,
                    seat
            ));
            sideTable.addCell(writeInSide(
                    new float[] {1, 1, 0.5f},
                    "Flight",
                    "Date",
                    "Time",
                    ticket.getFlightNumber(),
                    ticket.getExpectedDepartureDatetime().toLocalDate().toString(),
                    ticket.getExpectedDepartureDatetime().toLocalTime().truncatedTo(ChronoUnit.MINUTES).toString()
            ));

            PdfPCell emptyCell = new PdfPCell(new Phrase());
            disableCellBorders(emptyCell);
            baseTable.addCell(emptyCell);
            sideTable.addCell(writeInStyle(
                    PdfTicketGenerator::setToLastSideLineContentWrapperStyle,
                    new float[] {1, 1, 0.5f},
                "Gate",
                    "Boarding till",
                    "Seat",
                    gate,
                    boardingTill,
                    seat
            ));

            baseTable.addCell(createFooter(ticket));
            sideTable.addCell(createEmptyFooter());

            PdfPCell baseCell = new PdfPCell(baseTable);
            PdfPCell sideCell = new PdfPCell(sideTable);
            disableAllCellsBorders(baseCell, sideCell);

            rootTable.addCell(baseCell);
            rootTable.addCell(sideCell);
            document.add(rootTable);
            document.add(new Chunk(""));
            document.close();

        } catch (DocumentException ex) {
            log.error("Could not make a printable ticket pdf", ex);
            throw new PrintException("Server failed to make printable ticket. Try later.", ex);
        }
    }

    private static void fillMetaData(Document document, PrintableTicket ticket) {
        document.addTitle("Boarding pass");
        document.addSubject("Ticket on flight " + ticket.getFlightNumber());
        document.addKeywords("Flight, ticket, boarding, airport, " + ticket.getArrivalCity());
        document.addCreator(ticket.getAirlineName());
        document.addCreationDate();
    }

    private static void setToHeaderStyle(Font ... fonts) {
        for (Font font : fonts) {
            font.setColor(BaseColor.WHITE);
        }
    }

    private static void setToHeaderStyle(PdfPCell ... cells) {
        for (PdfPCell cell : cells) {
            cell.setBackgroundColor(BaseColor.BLUE);
            disableCellBorders(cell);
            cell.setFixedHeight(45);
        }
    }

    private static void setToBaseContentWrapperStyle(PdfPCell cell) {
        cell.setPaddingLeft(5);
        disableCellBorders(cell);
        cell.setMinimumHeight(35);
    }

    private static void setToSideContentWrapperStyle(PdfPCell cell) {
        cell.setPaddingLeft(5);
        disableCellBorders(cell);
        cell.setMinimumHeight(26);
    }

    private static void setToLastBaseLineContentWrapperStyle(PdfPCell cell) {
        cell.setPaddingLeft(5);
        disableCellBorders(cell);
        cell.setMinimumHeight(30);
    }

    private static void setToLastSideLineContentWrapperStyle(PdfPCell cell) {
        cell.setPaddingLeft(5);
        disableCellBorders(cell);
        cell.setMinimumHeight(31);
    }

    private static void setToFooterStyle(PdfPCell ... footerPieces) {
        for (PdfPCell footerPiece : footerPieces) {
            setToFooterStyle(footerPiece);
        }
    }

    private static void setToFooterStyle(PdfPCell footer) {
        footer.setMinimumHeight(25);
        footer.setBackgroundColor(BaseColor.BLUE);
        disableCellBorders(footer);
    }

    private static PdfPCell writeBaseHeader(PrintableTicket ticket) throws DocumentException {

        PdfPTable headerTable = new PdfPTable(3);
        headerTable.setWidthPercentage(100);
        headerTable.setWidths(new float[] {1, 1.8f, 4});

        Font logoFont = new Font(Font.FontFamily.HELVETICA, 12, Font.BOLDITALIC);
        Font titleFont = new Font(Font.FontFamily.TIMES_ROMAN, 14, Font.NORMAL);
        setToHeaderStyle(logoFont, titleFont);

        PdfPCell logoImg;
        String iconsPath = new ClassPathResource("/src/main/resources/icons/logo.png").getPath();
        Image img;
        try {
            img = Image.getInstance(iconsPath);
            logoImg = new PdfPCell(img, true);
            logoImg.setPadding(5);
        } catch (IOException e) {
            log.error("Could not load logo for printing a ticket", e);
            logoImg = new PdfPCell();
        }

        PdfPCell logoName;
        Phrase airlineName = new Phrase(ticket.getAirlineName(), logoFont);
        logoName = new PdfPCell(airlineName);
        logoName.setVerticalAlignment(Element.ALIGN_MIDDLE);
        logoName.setHorizontalAlignment(Element.ALIGN_LEFT);

        PdfPCell title;
        Phrase titleInscription = new Phrase("BOARDING PASS", titleFont);
        title = new PdfPCell(titleInscription);
        title.setHorizontalAlignment(Element.ALIGN_CENTER);

        setToHeaderStyle(logoName, title, logoImg);

        headerTable.addCell(logoImg);
        headerTable.addCell(logoName);
        headerTable.addCell(title);
        return createDullWrapperCell(headerTable);
    }

    private static PdfPCell createDullWrapperCell(PdfPTable table) {
        PdfPCell cellWrapper = new PdfPCell(table);
        disableCellBorders(cellWrapper);
        return cellWrapper;
    }

    private static PdfPCell writeSideHeader(PrintableTicket ticket) {

        PdfPTable headerTable = new PdfPTable(1);
        headerTable.setWidthPercentage(100);

        Font titleFont = new Font(Font.FontFamily.TIMES_ROMAN, 14, Font.ITALIC);
        Font seatTypeFont = new Font(Font.FontFamily.TIMES_ROMAN, 12, Font.ITALIC);
        setToHeaderStyle(titleFont, seatTypeFont);

        PdfPCell title;
        Phrase titleInscription = new Phrase("Have a nice flight", titleFont);
        title = new PdfPCell(titleInscription);
        title.setHorizontalAlignment(Element.ALIGN_CENTER);
        disableCellBorders(title);

        PdfPCell seatClass;
        Phrase classInscription = new Phrase("in " + ticket.getSeatType() + " class", seatTypeFont);
        seatClass = new PdfPCell(classInscription);
        seatClass.setHorizontalAlignment(Element.ALIGN_RIGHT);
        seatClass.setVerticalAlignment(Element.ALIGN_TOP);
        disableCellBorders(seatClass);

        headerTable.addCell(title);
        headerTable.addCell(seatClass);
        PdfPCell cellWrapper = new PdfPCell(headerTable);
        setToHeaderStyle(cellWrapper);
        return cellWrapper;
    }

    private static void disableCellBorders(PdfPCell cell) {
        cell.setBorder(Rectangle.NO_BORDER);
    }

    private static void disableAllCellsBorders(PdfPCell ... cells) {
        for (PdfPCell cell : cells) {
            disableCellBorders(cell);
        }
    }

    private static PdfPCell createStyledWrapperCell(PdfPTable table, Consumer<PdfPCell> stylelist) {
        PdfPCell cellWrapper = new PdfPCell(table);
        stylelist.accept(cellWrapper);
        return cellWrapper;
    }

    private static void writeLine(PdfPTable wrapper, Phrase ... phrases) {
        for (Phrase phrase : phrases) {
            PdfPCell cell = new PdfPCell(phrase);
            disableCellBorders(cell);
            wrapper.addCell(cell);
        }
    }

    private static float[] getBalancedWidths(int propertyValuePairs) {
        float[] widths = new float[propertyValuePairs / 2];
        Arrays.fill(widths, 1);
        return widths;
    }

    private static PdfPTable writePropertiesValues(float[] widths, String ... propertiesValues) {
        if (propertiesValues.length % 2 != 0) {
            throw new UnsupportedOperationException("Number of properties doesn't equal to number of values");
        }
        int i = 0;
        int argumentsPairs = propertiesValues.length / 2;
        Phrase[] inscriptions = new Phrase[argumentsPairs];
        Phrase[] fields = new Phrase[argumentsPairs];

        PdfPTable tableWrapper = new PdfPTable(widths);
        while (i < argumentsPairs) {
            inscriptions[i] = new Phrase(propertiesValues[i++], CELLS_CONTENT_TITLES);
        }
        while (i < propertiesValues.length) {
            fields[i - argumentsPairs] = new Phrase(propertiesValues[i++], CELLS_CONTENT_FIELDS);
        }
        writeLine(tableWrapper, inscriptions);
        writeLine(tableWrapper, fields);
        return tableWrapper;
    }

    private static PdfPCell writeInStyle(Consumer<PdfPCell> stylist, float[] widths, String ... propertiesValues) {
        return createStyledWrapperCell(writePropertiesValues(widths, propertiesValues), stylist);
    }

    private static PdfPCell writeInBase(String ... propertiesValues) {
        return writeInStyle(
                PdfTicketGenerator::setToBaseContentWrapperStyle,
                getBalancedWidths(propertiesValues.length),
                propertiesValues);
    }

    private static PdfPCell writeInBase(float[] widths, String ... propertiesValues) {
        return writeInStyle(
                PdfTicketGenerator::setToBaseContentWrapperStyle,
                widths,
                propertiesValues);
    }

    private static PdfPCell writeInSide(String ... propertiesValues) {
        return writeInStyle(
                PdfTicketGenerator::setToSideContentWrapperStyle,
                getBalancedWidths(propertiesValues.length),
                propertiesValues);
    }

    private static PdfPCell writeInSide(float[] widths, String ... propertiesValues) {
        return writeInStyle(
                PdfTicketGenerator::setToSideContentWrapperStyle,
                widths,
                propertiesValues);
    }

    private static PdfPCell createFooter(PrintableTicket ticket) {
        PdfPTable tableWrapper = new PdfPTable(new float[] {1.5f, 1, 0.1f});
        Font footerFont = new Font(Font.FontFamily.HELVETICA, 7, Font.ITALIC);
        footerFont.setColor(BaseColor.LIGHT_GRAY);

        Phrase mailPhr = new Phrase(ticket.getAirlineEmail(), footerFont);
        PdfPCell mail = new PdfPCell(mailPhr);

        Phrase phonePhr = new Phrase(ticket.getAirlinePhoneNumber(), footerFont);
        PdfPCell phone = new PdfPCell(phonePhr);

        PdfPCell emptyCell = new PdfPCell();
        setToFooterStyle(mail, phone, emptyCell);
        tableWrapper.addCell(mail);
        tableWrapper.addCell(phone);
        tableWrapper.addCell(emptyCell);
        return createDullWrapperCell(tableWrapper);
    }

    private static PdfPCell createEmptyFooter() {
        PdfPCell footer = new PdfPCell();
        setToFooterStyle(footer);
        return footer;
    }
}
