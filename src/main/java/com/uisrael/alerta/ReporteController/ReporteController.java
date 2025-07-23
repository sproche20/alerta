package com.uisrael.alerta.ReporteController;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;

import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Font;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;

import jakarta.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import com.uisrael.alerta.model.IncidenteModel;
import com.uisrael.alerta.model.NotificacionModel;
import com.uisrael.alerta.service.IncidenteService;
import com.uisrael.alerta.service.NotificacionService;

@Controller
@RequestMapping("/reportes")
public class ReporteController {

    @Autowired
    private IncidenteService incidenteService;

    @Autowired
    private NotificacionService notificacionService;

    @GetMapping("/incidentes/csv")
    public void exportarIncidentesCSV(HttpServletResponse response) throws IOException {
        response.setContentType("text/csv");
        response.setHeader("Content-Disposition", "attachment; filename=incidentes.csv");

        List<IncidenteModel> incidentes = incidenteService.listar();
        PrintWriter writer = response.getWriter();

        writer.println("Tipo,Descripción,Dirección,Fecha y Hora,Estado,Usuario");
        for (IncidenteModel inc : incidentes) {
            writer.println(String.join(",",
                inc.getTipo(),
                inc.getDescripcion(),
                inc.getDireccion(),
                inc.getFechaHora().toString(),
                inc.getEstado(),
                inc.getUsuarioModel() != null ? inc.getUsuarioModel().getNombre() : "N/A"
            ));
        }

        writer.flush();
        writer.close();
    }

    @GetMapping("/notificaciones/csv")
    public void exportarNotificacionesCSV(HttpServletResponse response) throws IOException {
        response.setContentType("text/csv");
        response.setHeader("Content-Disposition", "attachment; filename=notificaciones.csv");

        List<NotificacionModel> notificaciones = notificacionService.listar();
        PrintWriter writer = response.getWriter();

        writer.println("Mensaje,Fecha Envío,Tipo de Incidente,Barrio");
        for (NotificacionModel n : notificaciones) {
            writer.println(String.join(",",
                n.getMensaje(),
                n.getFechaEnvio().toString(),
                n.getIncidenteModel() != null ? n.getIncidenteModel().getTipo() : "---",
                n.getIncidenteModel() != null && n.getIncidenteModel().getBarrioModel() != null ?
                    n.getIncidenteModel().getBarrioModel().getNombre() : "N/A"
            ));
        }

        writer.flush();
        writer.close();
    }

    @GetMapping("/incidentes/pdf")
    public void exportarIncidentesPDF(HttpServletResponse response) throws IOException, DocumentException {
        response.setContentType("application/pdf");
        response.setHeader("Content-Disposition", "attachment; filename=incidentes.pdf");

        Document document = new Document();
        PdfWriter.getInstance(document, response.getOutputStream());
        document.open();

        document.add(new Paragraph("REPORTE DE INCIDENTES", new Font(Font.FontFamily.HELVETICA, 18)));

        PdfPTable table = new PdfPTable(6);
        table.setWidthPercentage(100);

        table.addCell("Tipo");
        table.addCell("Descripción");
        table.addCell("Dirección");
        table.addCell("Fecha y Hora");
        table.addCell("Estado");
        table.addCell("Usuario");

        List<IncidenteModel> incidentes = incidenteService.listar();
        for (IncidenteModel inc : incidentes) {
            table.addCell(inc.getTipo());
            table.addCell(inc.getDescripcion());
            table.addCell(inc.getDireccion());
            table.addCell(inc.getFechaHora().toString());
            table.addCell(inc.getEstado());
            table.addCell(inc.getUsuarioModel() != null ? inc.getUsuarioModel().getNombre() : "N/A");
        }

        document.add(table);
        document.close();
    }

    @GetMapping("/notificaciones/pdf")
    public void exportarNotificacionesPDF(HttpServletResponse response) throws IOException, DocumentException {
        response.setContentType("application/pdf");
        response.setHeader("Content-Disposition", "attachment; filename=notificaciones.pdf");

        Document document = new Document();
        PdfWriter.getInstance(document, response.getOutputStream());
        document.open();

        document.add(new Paragraph("REPORTE DE NOTIFICACIONES", new Font(Font.FontFamily.HELVETICA, 18)));

        PdfPTable table = new PdfPTable(4);
        table.setWidthPercentage(100);

        table.addCell("Mensaje");
        table.addCell("Fecha Envío");
        table.addCell("Tipo Incidente");
        table.addCell("Barrio");

        List<NotificacionModel> notificaciones = notificacionService.listar();
        for (NotificacionModel n : notificaciones) {
            table.addCell(n.getMensaje());
            table.addCell(n.getFechaEnvio().toString());
            table.addCell(n.getIncidenteModel() != null ? n.getIncidenteModel().getTipo() : "---");
            table.addCell(
                n.getIncidenteModel() != null && n.getIncidenteModel().getBarrioModel() != null ?
                n.getIncidenteModel().getBarrioModel().getNombre() : "N/A"
            );
        }

        document.add(table);
        document.close();
    }
}
