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

@Log4j2
public class TicketDocument {
    private final static Font CELLS_CONTENT_TITLES = new Font(Font.FontFamily.HELVETICA, 8, Font.UNDERLINE);
    private final static Font CELLS_CONTENT_FIELDS = new Font(Font.FontFamily.COURIER, 8, Font.NORMAL);
    private PrintableTicket ticket;
    private Document document;

    public Document createTicketDocument(PrintableTicket ticket, OutputStream outputStream) {
        Rectangle rectangle = new Rectangle(PageSize.A6.rotate());
        this.document = new Document(rectangle);
        document.setMargins(0, 0, 0, 0);
        this.ticket = ticket;

        try {
            PdfWriter.getInstance(document, outputStream);
            document.open();
            fillMetaData();

            PdfPTable rootTable = new PdfPTable(new float[] {1, 0.6f});
            rootTable.setWidthPercentage(100);

            rootTable.addCell(writeBaseHeader());
            rootTable.addCell(writeSideHeader());

            rootTable.addCell(writePassengerName());
            rootTable.addCell(writePassengerName());

            rootTable.addCell(writeInBase(
                "From",
                "Date",
                "Time",
                ticket.getDepartureCity(),
                ticket.getExpectedDepartureDatetime().toLocalDate().toString(),
                ticket.getExpectedDepartureDatetime().toLocalTime().truncatedTo(ChronoUnit.MINUTES).toString()
            ));
            rootTable.addCell(writeInSide(
                "From",
                ticket.getDepartureCity()
            ));

            rootTable.addCell(writeInBase(
                "To",
                "Flight",
                ticket.getArrivalCity(),
                ticket.getFlightNumber()
            ));
            rootTable.addCell(writeInSide(
                "To",
                ticket.getArrivalCity()
            ));

            int gateNum = Integer.valueOf(ticket.getFlightNumber().replaceAll("\\D+","")) % 10;
            String gate = gateNum == 0 ? "10" : "0" + gateNum;
            String boardingTill = ticket.getExpectedArrivalDatetime().toLocalTime().truncatedTo(ChronoUnit.MINUTES).minusMinutes(30).toString();
            String seat = "" + ticket.getSeatRow() + (char) ticket.getSeatColumn();
            rootTable.addCell(writeInBase(
                "Gate",
                "Boarding till",
                "Seat",
                gate,
                boardingTill,
                seat
            ));
            rootTable.addCell(writeInSide(
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
            rootTable.addCell(emptyCell);
            rootTable.addCell(writeInSide(
                "Gate",
                    "Boarding till",
                    "Seat",
                    gate,
                    boardingTill,
                    seat
            ));

            rootTable.addCell(createFooter());
            rootTable.addCell(createFooter());

            document.add(rootTable);
            document.add(new Chunk(""));
            document.close();

        } catch (DocumentException ex) {
            log.error("Could not make a printable ticket pdf", ex);
            throw new PrintException("Server failed to make printable ticket. Try later.", ex);
        }
        return null;
    }

    private void fillMetaData() {
        document.addTitle("Boarding pass");
        document.addSubject("Ticket on flight " + ticket.getFlightNumber());
        document.addKeywords("Flight, ticket, boarding, airport, " + ticket.getArrivalCity());
        document.addCreator(ticket.getAirlineName());
        document.addCreationDate();
    }

    private PdfPCell writeBaseHeader() throws DocumentException {

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

        PdfPCell title;
        Phrase titleInscription = new Phrase("BOARDING PASS", titleFont);
        title = new PdfPCell(titleInscription);
        title.setHorizontalAlignment(Element.ALIGN_CENTER);

        setToHeaderStyle(logoName, title, logoImg);

        headerTable.addCell(logoImg);
        headerTable.addCell(logoName);
        headerTable.addCell(title);
        PdfPCell cellWrapper = new PdfPCell(headerTable);
        disableCellBorders(cellWrapper);
        return cellWrapper;
    }

    private PdfPCell writeSideHeader() {

        PdfPTable headerTable = new PdfPTable(1);
        headerTable.setWidthPercentage(100);

        Font titleFont = new Font(Font.FontFamily.TIMES_ROMAN, 14, Font.ITALIC);
        setToHeaderStyle(titleFont);

        PdfPCell title;
        Phrase titleInscription = new Phrase("Have a nice flight", titleFont);
        title = new PdfPCell(titleInscription);
        title.setHorizontalAlignment(Element.ALIGN_CENTER);

        setToHeaderStyle(title);

        headerTable.addCell(title);
        PdfPCell cellWrapper = new PdfPCell(headerTable);
        disableCellBorders(cellWrapper);
        return cellWrapper;
    }

    private void disableCellBorders(PdfPCell cell) {
        cell.setBorder(Rectangle.NO_BORDER);
    }

    private void disableAllCellsBorders(PdfPCell ... cells) {
        for (PdfPCell cell : cells) {
            disableCellBorders(cell);
        }
    }

    private void setToHeaderStyle(Font ... fonts) {
        for (Font font : fonts) {
            font.setColor(BaseColor.WHITE);
        }
    }

    private void setToHeaderStyle(PdfPCell ... cells) {
        for (PdfPCell cell : cells) {
            cell.setBackgroundColor(BaseColor.BLUE);
            disableCellBorders(cell);
            cell.setFixedHeight(45);
        }
    }

    private PdfPCell writePassengerName() {
        PdfPTable tableWrapper = new PdfPTable(1);
        Phrase nameInscription = new Phrase("Name of passenger", CELLS_CONTENT_TITLES);
        Phrase actualName = new Phrase(ticket.getPassengerFirstName() + " " + ticket.getPassengerLastName(), CELLS_CONTENT_FIELDS);
        writeLine(tableWrapper, nameInscription, actualName);
        return createBaseWrapperCell(tableWrapper);
    }

    private PdfPCell createBaseWrapperCell(PdfPTable table) {
        PdfPCell cellWrapper = new PdfPCell(table);
        setToContentWrapperStyle(cellWrapper);
        return cellWrapper;
    }

    private PdfPCell createSizeWrapperCell(PdfPTable table) {
        PdfPCell cellWrapper = new PdfPCell(table);
        setToContentWrapperStyle(cellWrapper);
        return cellWrapper;
    }

    private void setToContentWrapperStyle(PdfPCell cell) {
        cell.setPaddingLeft(5);
        disableCellBorders(cell);
        cell.setMinimumHeight(35);
    }

    private void writeLine(PdfPTable wrapper, Phrase ... phrases) {
        for (Phrase phrase : phrases) {
            PdfPCell cell = new PdfPCell(phrase);
            disableCellBorders(cell);
            wrapper.addCell(cell);
        }
    }

    private PdfPTable writePropertiesValues(String ... propertiesValues) {
        if (propertiesValues.length % 2 != 0) {
            throw new UnsupportedOperationException("Number of properties doesn't equals to number of values");
        }
        int i = 0;
        int argumentsPairs = propertiesValues.length / 2;
        Phrase[] inscriptions = new Phrase[argumentsPairs];
        Phrase[] fields = new Phrase[argumentsPairs];

        PdfPTable tableWrapper = new PdfPTable(argumentsPairs);
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

    private PdfPTable writePropertiesValues(float[] widths, String ... propertiesValues) {
        if (propertiesValues.length % 2 != 0) {
            throw new UnsupportedOperationException("Number of properties doesn't equals to number of values");
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

    private PdfPCell writeInBase(String ... propertiesValues) {
        return createBaseWrapperCell(writePropertiesValues(propertiesValues));
    }

    private PdfPCell writeInBase(float[] widths, String ... propertiesValues) {
        return createBaseWrapperCell(writePropertiesValues(widths, propertiesValues));
    }

    private PdfPCell writeInSide(String ... propertiesValues) {
        return createSizeWrapperCell(writePropertiesValues(propertiesValues));
    }

    private PdfPCell writeInSide(float[] widths, String ... propertiesValues) {
        return createSizeWrapperCell(writePropertiesValues(widths, propertiesValues));
    }

    private PdfPCell createFooter() {
        PdfPTable tableWrapper = new PdfPTable(new float[] {0.5f, 0.5f, 1});
        Font footerFont = new Font(Font.FontFamily.HELVETICA, 7, Font.ITALIC);
        footerFont.setColor(BaseColor.LIGHT_GRAY);

        Phrase mailPhr = new Phrase(ticket.getAirlineEmail(), footerFont);
        PdfPCell mail = new PdfPCell(mailPhr);

        Phrase phonePhr = new Phrase(ticket.getAirlinePhoneNumber(), footerFont);
        PdfPCell phone = new PdfPCell(phonePhr);

        PdfPCell emptyCell = new PdfPCell();
        disableAllCellsBorders(mail, phone, emptyCell);
        tableWrapper.addCell(mail);
        tableWrapper.addCell(phone);
        tableWrapper.addCell(emptyCell);
        PdfPCell cellWrapper = new PdfPCell(tableWrapper);
        disableCellBorders(cellWrapper);
        return cellWrapper;
    }

    private void setToFooterStyle(PdfPCell footer) {
        footer.setMinimumHeight(25);
        footer.setBackgroundColor(BaseColor.BLUE);
        disableCellBorders(footer);
    }
}
