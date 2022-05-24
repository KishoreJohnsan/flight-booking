package com.flightapp.bookingservice.service;

import com.flightapp.bookingservice.entity.Booking;
import com.flightapp.bookingservice.exception.BookingAlreadyExistsException;
import com.flightapp.bookingservice.exception.BookingNotFoundException;
import com.flightapp.bookingservice.repo.BookingRepo;
import com.itextpdf.barcodes.BarcodeQRCode;
import com.itextpdf.io.image.ImageDataFactory;
import com.itextpdf.kernel.colors.ColorConstants;
import com.itextpdf.kernel.geom.PageSize;
import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.kernel.pdf.xobject.PdfFormXObject;
import com.itextpdf.layout.Document;
import com.itextpdf.layout.borders.Border;
import com.itextpdf.layout.element.*;
import com.itextpdf.layout.properties.HorizontalAlignment;
import com.itextpdf.layout.properties.TextAlignment;
import com.itextpdf.layout.properties.VerticalAlignment;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Service
public class BookingServiceImpl implements BookingService {

    @Autowired
    private BookingRepo repo;

    @Autowired
    private Environment env;

    @Override
    public List<Booking> getBookingByUser(String user) throws BookingNotFoundException {
        List<Booking> bookingList = repo.findByUser(user);
        if (bookingList.isEmpty())
            throw new BookingNotFoundException();
        else
            return bookingList;
    }

    @Override
    public Booking getBookingById(Long bookingId) throws BookingNotFoundException {
        Optional<Booking> bookingOpt = repo.findById(bookingId);
        if (bookingOpt.isPresent())
            return bookingOpt.get();
        else
            throw new BookingNotFoundException();
    }

    @Override
    public boolean saveBooking(Booking booking) throws BookingAlreadyExistsException {
        Optional<Booking> bookingOpt = repo.findByUserAndSourceAndDestinationAndDateAndTimeAndStatusEqualsIgnoreCase(
                booking.getUser(), booking.getSource(), booking.getDestination(), booking.getDate(), booking.getTime(), "BOOKED"
        );
        if (bookingOpt.isPresent())
            throw new BookingAlreadyExistsException();
        else {
            booking.setStatus("BOOKED");
            repo.save(booking);
        }

        return true;
    }

    @Override
    public boolean updateBooking(Booking booking) throws BookingNotFoundException {
        Optional<Booking> bookingOpt = repo.findById(booking.getBookingId());
        if (bookingOpt.isPresent())
            repo.save(booking);
        else
            throw new BookingNotFoundException();

        return true;
    }

    @Override
    public boolean cancelBooking(Long bookingId) throws BookingNotFoundException {
        Optional<Booking> bookingOpt = repo.findById(bookingId);
        if (bookingOpt.isPresent()) {
            Booking booking = bookingOpt.get();
            booking.setStatus("CANCELLED");
            repo.save(booking);
        } else
            throw new BookingNotFoundException();

        return true;
    }

    @Override
    public boolean deleteBooking(Long bookingId) throws BookingNotFoundException {
        Optional<Booking> bookingOpt = repo.findById(bookingId);
        if (bookingOpt.isPresent())
            repo.deleteById(bookingId);
        else
            throw new BookingNotFoundException();

        return true;
    }

    @Override
    public Map<String, Object> generateTicket(Long bookingId) throws BookingNotFoundException, IOException {
        Map<String, Object> map = new HashMap<>();

        Optional<Booking> bookingOpt = repo.findById(bookingId);
        if (bookingOpt.isPresent()) {
            Booking booking = bookingOpt.get();
            File ticket = generateTicketPDF(booking);
            map.put("User", booking.getUser());
            map.put("Ticket", ticket);
            return map;
        } else
            throw new BookingNotFoundException();
    }

    private File generateTicketPDF(Booking booking) throws IOException {

        String dest = env.getProperty("ticket.temp.path");
        PdfWriter writer = new PdfWriter(dest);
        PdfDocument pdfDocument = new PdfDocument(writer);

        Document document = new Document(pdfDocument, PageSize.A4);
        document.setMargins(50, 50, 50, 50);

        String logoDest = env.getProperty("logo.path");
        String airlineLogoDest = env.getProperty("indigo.logo.path");

        Image logo = new Image(ImageDataFactory.create(logoDest));
        Image airlineLogo = new Image(ImageDataFactory.create(airlineLogoDest));

        BarcodeQRCode qrCode = new BarcodeQRCode(booking.toString());
        PdfFormXObject barcodeObject = qrCode.createFormXObject(ColorConstants.BLACK, pdfDocument);
        Image barcodeImage = new Image(barcodeObject).setWidth(85f).setHeight(85f);

        logo.setAutoScale(true);
        airlineLogo.setAutoScale(true);

        Cell cell1 = new Cell().add(logo);
        cell1.setHorizontalAlignment(HorizontalAlignment.LEFT);
        cell1.setVerticalAlignment(VerticalAlignment.BOTTOM);
        cell1.setBorder(Border.NO_BORDER);

        Cell cell2 = new Cell().add(barcodeImage);
        cell2.setHorizontalAlignment(HorizontalAlignment.CENTER);
        cell2.setVerticalAlignment(VerticalAlignment.MIDDLE);
        cell2.setPaddingLeft(70F);
        cell2.setBorder(Border.NO_BORDER);

        Cell cell3 = new Cell().add(airlineLogo);
        cell3.setHorizontalAlignment(HorizontalAlignment.RIGHT);
        cell3.setVerticalAlignment(VerticalAlignment.BOTTOM);
        cell3.setBorder(Border.NO_BORDER);

        float[] logoTableWidths = {130F, 100F, 135F};
        Table logoTable = pdfTable(logoTableWidths);
        logoTable.addCell(cell1);
        logoTable.addCell(cell2);
        logoTable.addCell(cell3);
        document.add(logoTable);

        document.add(new Paragraph("\n"));

        Cell pnrCellHeading = new Cell().add(new Paragraph("PNR")).setBackgroundColor(ColorConstants.LIGHT_GRAY);
        Cell fltNoCellHeading = new Cell().add(new Paragraph("Flight Number")).setBackgroundColor(ColorConstants.LIGHT_GRAY);
        Cell fltTypeHeading = new Cell().add(new Paragraph("Flight Type")).setBackgroundColor(ColorConstants.LIGHT_GRAY);

        Cell pnrDataCell = new Cell().add(new Paragraph(booking.getBookingId().toString()));
        Cell fltNoCellData = new Cell();
        Cell fltTypeData = new Cell();

        float[] infoTable1Widths = {150F, 150F, 150F};
        Table infoTable1 = pdfTable(infoTable1Widths);
        infoTable1.addCell(pnrCellHeading);
        infoTable1.addCell(fltNoCellHeading);
        infoTable1.addCell(fltTypeHeading);
        infoTable1.addCell(pnrDataCell);
        infoTable1.addCell(fltNoCellData);
        infoTable1.addCell(fltTypeData);

        document.add(infoTable1);
        document.add(new Paragraph("\n"));

        Cell sourceHeading = new Cell().add(new Paragraph("Source")).setBackgroundColor(ColorConstants.LIGHT_GRAY);
        sourceHeading.setTextAlignment(TextAlignment.CENTER);
        Cell destHeading = new Cell().add(new Paragraph("Destination")).setBackgroundColor(ColorConstants.LIGHT_GRAY);
        destHeading.setTextAlignment(TextAlignment.CENTER);
        Cell sourceData = new Cell().add(new Paragraph(booking.getSource()));
        sourceData.setTextAlignment(TextAlignment.CENTER);
        Cell destData = new Cell().add(new Paragraph(booking.getDestination()));
        destData.setTextAlignment(TextAlignment.CENTER);

        float[] infoTable2Widths = {150F, 150F};
        Table infoTable2 = pdfTable(infoTable2Widths);
        infoTable2.addCell(sourceHeading);
        infoTable2.addCell(destHeading);
        infoTable2.addCell(sourceData);
        infoTable2.addCell(destData);

        document.add(infoTable2);
        document.add(new Paragraph("\n"));

        Cell deptDtHeading = new Cell().add(new Paragraph("Source")).setBackgroundColor(ColorConstants.LIGHT_GRAY);
        sourceHeading.setTextAlignment(TextAlignment.CENTER);
        Cell deptTimeHeading = new Cell().add(new Paragraph("Destination")).setBackgroundColor(ColorConstants.LIGHT_GRAY);
        destHeading.setTextAlignment(TextAlignment.CENTER);
        Cell deptDtData = new Cell().add(new Paragraph(booking.getDate()));
        sourceData.setTextAlignment(TextAlignment.CENTER);
        Cell deptTimeData = new Cell().add(new Paragraph(booking.getTime()));
        destData.setTextAlignment(TextAlignment.CENTER);

        float[] infoTable3Widths = {150F, 150F};
        Table infoTable3 = pdfTable(infoTable3Widths);
        infoTable3.addCell(deptDtHeading);
        infoTable3.addCell(deptTimeHeading);
        infoTable3.addCell(deptDtData);
        infoTable3.addCell(deptTimeData);

        document.add(infoTable3);
        document.add(new Paragraph("\n"));

        Cell seatsHeading = new Cell().add(new Paragraph("Seats")).setBackgroundColor(ColorConstants.LIGHT_GRAY);
        Cell fareHeading = new Cell().add(new Paragraph("Fare")).setBackgroundColor(ColorConstants.LIGHT_GRAY);
        Cell prefHeading = new Cell().add(new Paragraph("Meal Preference")).setBackgroundColor(ColorConstants.LIGHT_GRAY);
        Cell statusHeading = new Cell().add(new Paragraph("Status")).setBackgroundColor(ColorConstants.LIGHT_GRAY);

        Cell seatsData = new Cell().add(new Paragraph(booking.getSeats().toString())).setTextAlignment(TextAlignment.CENTER);
        Cell fareData = new Cell().add(new Paragraph(booking.getFare().toString())).setTextAlignment(TextAlignment.CENTER);
        Cell prefData = new Cell().add(new Paragraph(booking.getMealPreference().toUpperCase())).setTextAlignment(TextAlignment.CENTER);
        Cell statusData = new Cell().add(new Paragraph(booking.getStatus())).setTextAlignment(TextAlignment.CENTER);

        float[] infoTable4Widths = {100F, 150F};
        Table infoTable4 = pdfTable(infoTable4Widths);
        infoTable4.addCell(seatsHeading);
        infoTable4.addCell(seatsData);
        infoTable4.addCell(fareHeading);
        infoTable4.addCell(fareData);
        infoTable4.addCell(prefHeading);
        infoTable4.addCell(prefData);
        infoTable4.addCell(statusHeading);
        infoTable4.addCell(statusData);

        document.add(infoTable4);
        document.add(new Paragraph("\n"));
        document.close();

        File ticket = new File(dest);
        if(ticket.exists())
            return ticket;
        else
            throw new IOException("Ticket cannot be generated. Please try again later");
    }

    private Table pdfTable(float[] widths){

        Table table = new Table(widths);
        table.useAllAvailableWidth();
        return table;

    }


}
