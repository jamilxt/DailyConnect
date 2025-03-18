package dev.jamilxt.dailyconnect.controller.web;

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
        List<String> templates = Arrays.asList("Modern", "Classic", "Creative");
        model.addAttribute("portfolio", portfolio);
        model.addAttribute("templates", templates);
        return "resume";
    }

    @PostMapping("/resume/update")
    public String updatePortfolio(
            @RequestParam("fullName") String fullName,
            @RequestParam("email") String email,
            @RequestParam("phone") String phone,
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
        portfolio.setEmail(email);
        portfolio.setPhone(phone);
        portfolio.setIntro(intro);
        portfolio.setSkills(skills);
        portfolio.setProfessionalExperience(professionalExperience);
        portfolio.setProjects(projects);
        portfolio.setEducation(education);
        portfolioService.savePortfolio(portfolio);
        return "redirect:/resume";
    }

    @GetMapping("/resume/export")
    public ResponseEntity<byte[]> exportResume(@RequestParam("template") String template, Authentication authentication) throws Exception {
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

        // Customize based on template
        switch (template) {
            case "Modern":
                document.add(new Paragraph(new Text(portfolio.getFullName()).setFontSize(20).setBold()).setTextAlignment(TextAlignment.CENTER));
                document.add(new Paragraph("Email: " + portfolio.getEmail() + " | Phone: " + portfolio.getPhone()).setTextAlignment(TextAlignment.CENTER));
                document.add(new Paragraph("\n")); // Spacer
                document.add(new Paragraph("Intro").setFontSize(14).setBold());
                document.add(new Paragraph(portfolio.getIntro()));
                document.add(new Paragraph("Skills").setFontSize(14).setBold());
                document.add(new Paragraph(portfolio.getSkills()));
                document.add(new Paragraph("Professional Experience").setFontSize(14).setBold());
                document.add(new Paragraph(portfolio.getProfessionalExperience()));
                document.add(new Paragraph("Projects").setFontSize(14).setBold());
                document.add(new Paragraph(portfolio.getProjects()));
                document.add(new Paragraph("Education").setFontSize(14).setBold());
                document.add(new Paragraph(portfolio.getEducation()));
                break;
            case "Classic":
                document.add(new Paragraph(new Text("Curriculum Vitae").setFontSize(20).setBold()).setTextAlignment(TextAlignment.CENTER));
                document.add(new Paragraph(portfolio.getFullName()).setFontSize(14).setTextAlignment(TextAlignment.CENTER));
                document.add(new Paragraph("Contact: " + portfolio.getEmail() + " | " + portfolio.getPhone()).setTextAlignment(TextAlignment.CENTER));
                document.add(new Paragraph("\n")); // Spacer
                document.add(new Paragraph("Introduction").setFontSize(14).setBold());
                document.add(new Paragraph(portfolio.getIntro()));
                document.add(new Paragraph("Technical Skills").setFontSize(14).setBold());
                document.add(new Paragraph(portfolio.getSkills()));
                document.add(new Paragraph("Work Experience").setFontSize(14).setBold());
                document.add(new Paragraph(portfolio.getProfessionalExperience()));
                document.add(new Paragraph("Key Projects").setFontSize(14).setBold());
                document.add(new Paragraph(portfolio.getProjects()));
                document.add(new Paragraph("Education").setFontSize(14).setBold());
                document.add(new Paragraph(portfolio.getEducation()));
                break;
            case "Creative":
                document.add(new Paragraph(new Text("My Professional Journey").setFontSize(20).setBold()).setTextAlignment(TextAlignment.CENTER));
                document.add(new Paragraph("I am " + portfolio.getFullName()).setFontSize(14).setTextAlignment(TextAlignment.CENTER));
                document.add(new Paragraph("Reach me at: " + portfolio.getEmail() + " | " + portfolio.getPhone()).setTextAlignment(TextAlignment.CENTER));
                document.add(new Paragraph("\n")); // Spacer
                document.add(new Paragraph("About Me").setFontSize(14).setBold());
                document.add(new Paragraph(portfolio.getIntro()));
                document.add(new Paragraph("My Expertise").setFontSize(14).setBold());
                document.add(new Paragraph(portfolio.getSkills()));
                document.add(new Paragraph("My Career Path").setFontSize(14).setBold());
                document.add(new Paragraph(portfolio.getProfessionalExperience()));
                document.add(new Paragraph("Projects I’ve Built").setFontSize(14).setBold());
                document.add(new Paragraph(portfolio.getProjects()));
                document.add(new Paragraph("My Education").setFontSize(14).setBold());
                document.add(new Paragraph(portfolio.getEducation()));
                break;
            default:
                document.add(new Paragraph("Invalid template selected."));
        }

        document.close();
        byte[] pdfBytes = baos.toByteArray();

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_PDF);
        headers.setContentDispositionFormData("attachment", "resume-" + template.toLowerCase() + ".pdf");
        headers.setCacheControl("must-revalidate, post-check=0, pre-check=0");

        return ResponseEntity.ok().headers(headers).body(pdfBytes);
    }
}
