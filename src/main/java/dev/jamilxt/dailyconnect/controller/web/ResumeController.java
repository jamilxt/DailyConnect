package dev.jamilxt.dailyconnect.controller.web;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.itextpdf.io.source.ByteArrayOutputStream;
import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.layout.Document;
import com.itextpdf.layout.element.Paragraph;
import com.itextpdf.layout.element.Text;
import com.itextpdf.layout.properties.TextAlignment;
import dev.jamilxt.dailyconnect.entity.Portfolio;
import dev.jamilxt.dailyconnect.servie.PortfolioService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Controller
public class ResumeController {

    @Autowired
    private PortfolioService portfolioService;

    @GetMapping("/resume")
    public String resume(Authentication authentication, Model model) {
        String username = authentication.getName();
        Optional<Portfolio> portfolioOptional = portfolioService.getPortfolioByUsername(username);
        Portfolio portfolio = portfolioOptional.orElse(new Portfolio());
        model.addAttribute("portfolio", portfolio);
        model.addAttribute("projectsJson", portfolio.getProjects() != null ? portfolio.getProjects() : "[]");
        model.addAttribute("experienceJson", portfolio.getProfessionalExperience() != null ? portfolio.getProfessionalExperience() : "[]");
        return "resume";
    }

    @PostMapping("/resume/update")
    public String updatePortfolio(
            @RequestParam("fullName") String fullName,
            @RequestParam("title") String title,
            @RequestParam("email") String email,
            @RequestParam("phone") String phone,
            @RequestParam("address") String address,
            @RequestParam("githubLink") String githubLink,
            @RequestParam("linkedinLink") String linkedinLink,
            @RequestParam(value = "photoUrl", required = false) String photoUrl,
            @RequestParam("intro") String intro,
            @RequestParam("skills") String skills,
            @RequestParam("professionalExperience") String professionalExperience,
            @RequestParam("projects") String projects,
            @RequestParam("education") String education,
            Authentication authentication) {
        String username = authentication.getName();
        Optional<Portfolio> portfolioOptional = portfolioService.getPortfolioByUsername(username);
        Portfolio portfolio = portfolioOptional.orElse(new Portfolio());
        portfolio.setUsername(username);
        portfolio.setFullName(fullName);
        portfolio.setTitle(title);
        portfolio.setEmail(email);
        portfolio.setPhone(phone);
        portfolio.setAddress(address);
        portfolio.setGithubLink(githubLink);
        portfolio.setLinkedinLink(linkedinLink);
        portfolio.setPhotoUrl(photoUrl);
        portfolio.setIntro(intro);
        portfolio.setSkills(skills);
        portfolio.setProfessionalExperience(professionalExperience);
        portfolio.setProjects(projects);
        portfolio.setEducation(education);
        portfolioService.savePortfolio(portfolio);
        return "redirect:/resume";
    }

    @GetMapping("/resume/export")
    public ResponseEntity<byte[]> exportResume(Authentication authentication) throws Exception {
        String username = authentication.getName();
        Optional<Portfolio> portfolioOptional = portfolioService.getPortfolioByUsername(username);
        if (!portfolioOptional.isPresent()) {
            return ResponseEntity.badRequest().body("No portfolio data found.".getBytes());
        }

        Portfolio portfolio = portfolioOptional.get();
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        PdfWriter writer = new PdfWriter(baos);
        PdfDocument pdf = new PdfDocument(writer);
        Document document = new Document(pdf);

        document.setMargins(20, 20, 20, 20);
        document.add(new Paragraph(new Text(portfolio.getFullName()).setFontSize(18).setBold()).setTextAlignment(TextAlignment.LEFT));
        document.add(new Paragraph(portfolio.getTitle()).setFontSize(12).setTextAlignment(TextAlignment.LEFT));
        document.add(new Paragraph("\n"));

        // Contact Info (mirroring FlowCV's layout)
        if (portfolio.getAddress() != null) {
            document.add(new Paragraph("📍 " + portfolio.getAddress()).setFontSize(10));
        }
        if (portfolio.getPhone() != null) {
            document.add(new Paragraph("📞 " + portfolio.getPhone()).setFontSize(10));
        }
        if (portfolio.getEmail() != null) {
            document.add(new Paragraph("📧 " + portfolio.getEmail()).setFontSize(10));
        }
        if (portfolio.getGithubLink() != null) {
            document.add(new Paragraph("🌐 " + portfolio.getGithubLink()).setFontSize(10));
        }
        if (portfolio.getLinkedinLink() != null) {
            document.add(new Paragraph("🔗 " + portfolio.getLinkedinLink()).setFontSize(10));
        }
        document.add(new Paragraph("\n"));

        // Sections
        addSection(document, "INTRO", portfolio.getIntro());
        addSection(document, "SKILLS", portfolio.getSkills());

        // Professional Experience (parse JSON)
        if (portfolio.getProfessionalExperience() != null && !portfolio.getProfessionalExperience().isEmpty()) {
            document.add(new Paragraph("PROFESSIONAL EXPERIENCE").setFontSize(14).setBold());
            ObjectMapper mapper = new ObjectMapper();
            List<Map<String, String>> experiences = mapper.readValue(portfolio.getProfessionalExperience(), List.class);
            for (Map<String, String> exp : experiences) {
                String period = exp.get("startDate") + " - " + exp.get("endDate");
                document.add(new Paragraph(exp.get("company")).setFontSize(12).setBold());
                document.add(new Paragraph(exp.get("role") + " | " + period).setFontSize(10));
                document.add(new Paragraph("\n"));
            }
        }

        // Projects (parse JSON)
        if (portfolio.getProjects() != null && !portfolio.getProjects().isEmpty()) {
            document.add(new Paragraph("PROJECTS").setFontSize(14).setBold());
            ObjectMapper mapper = new ObjectMapper();
            List<Map<String, String>> projects = mapper.readValue(portfolio.getProjects(), List.class);
            for (Map<String, String> project : projects) {
                String period = project.get("startDate") + " - " + project.get("endDate");
                document.add(new Paragraph(project.get("title")).setFontSize(12).setBold());
                document.add(new Paragraph(period).setFontSize(10));
                document.add(new Paragraph(project.get("description")).setFontSize(10));
                document.add(new Paragraph("\n"));
            }
        }

        addSection(document, "EDUCATION", portfolio.getEducation());

        document.close();
        byte[] pdfBytes = baos.toByteArray();

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_PDF);
        headers.setContentDispositionFormData("attachment", "resume.pdf");
        headers.setCacheControl("must-revalidate, post-check=0, pre-check=0");

        return ResponseEntity.ok().headers(headers).body(pdfBytes);
    }

    private void addSection(Document document, String title, String content) {
        if (content != null && !content.isEmpty()) {
            document.add(new Paragraph(title).setFontSize(14).setBold());
            String[] lines = content.split("\n");
            for (String line : lines) {
                document.add(new Paragraph(line.trim()).setFontSize(10));
            }
            document.add(new Paragraph("\n"));
        }
    }
}
