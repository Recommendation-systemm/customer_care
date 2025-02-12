package com.customer.care.controllers;

import com.customer.care.entities.*;
import com.customer.care.repositories.*;
import com.customer.care.services.ComplaintService;
import com.customer.care.services.FileStorageService;
import com.customer.care.services.SubCountyService;
import com.itextpdf.text.*;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.io.IOException;
import java.security.Principal;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.List;

@Controller
public class ComplaintController {

    @Autowired
    private ComplaintRepository complaintRepository;

    public List<Complaint> getComplaintsByUser(AppUser user) {
        return complaintRepository.findByUser(user);
    }

    @Autowired
    private CategoryRepository categoryRepository;

    @Autowired
    private SubcategoryRepository subcategoryRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private FileStorageService fileStorageService;
    @Autowired
    private ComplaintFileRepository complaintFileRepository;
    @Autowired
    private WardRepository wardRepository;

    @GetMapping
    public String index()
    {
        return "index";

    }
    @GetMapping("/home")
    public String home(Model model)
    {
        model.addAttribute("title", "Home Page");
        return "layout";

    }

    @GetMapping("/complaints/export-pdf")
    public void exportToPDF(
            @RequestParam("fromDate") @DateTimeFormat(pattern = "yyyy-MM-dd") Date fromDate,
            @RequestParam("toDate") @DateTimeFormat(pattern = "yyyy-MM-dd") Date toDate,
            HttpServletResponse response) throws IOException, DocumentException {

        response.setContentType("application/pdf");
        response.setHeader("Content-Disposition", "attachment; filename=complaints.pdf");

        List<Complaint> complaints = complaintService.getComplaintsByDateRange(fromDate, toDate);

        Document document = new Document();
        PdfWriter.getInstance(document, response.getOutputStream());
        document.open();

        // Add Title
        Font titleFont = FontFactory.getFont(FontFactory.HELVETICA_BOLD, 16, BaseColor.BLUE);
//        Font titleFont = new Font(Font.HELVETICA, 16, Font.BOLD);
        Paragraph title = new Paragraph("Complaint Report", titleFont);
        title.setAlignment(Element.ALIGN_CENTER);
        document.add(title);

        document.add(new Paragraph("\n"));

        // Create Table
        PdfPTable table = new PdfPTable(6); // 6 Columns: #, Title, Description, Date
        table.setWidthPercentage(100);
        table.setSpacingBefore(10f);

        // Table Headers
        String[] headers = {"#", "Title", "Details","Documents","Ward", "Date"};
        for (String header : headers) {
            PdfPCell cell = new PdfPCell(new Phrase(header));
            cell.setHorizontalAlignment(Element.ALIGN_CENTER);
            cell.setBackgroundColor(BaseColor.LIGHT_GRAY);
            table.addCell(cell);
        }

        // Table Data
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        int counter = 1;
        for (Complaint complaint : complaints) {
            table.addCell(String.valueOf(counter++));
            table.addCell(complaint.getTitle());
            table.addCell(complaint.getDescription());
            table.addCell(String.valueOf(complaint.getFiles().size()));
            table.addCell(complaint.getWard() != null ? complaint.getWard().getWardName() : "None");
            table.addCell(dateFormat.format(complaint.getCreatedAt())); // Assuming `createdAt` exists
        }

        document.add(table);
        document.close();
    }




    @GetMapping("/complaints/{uuid}")
    public String getComplaintDetails(@PathVariable UUID uuid, Model model) {
        Complaint complaint = complaintRepository.findByUuid(uuid)
                .orElseThrow(() -> new RuntimeException("Complaint not found"));

        model.addAttribute("complaint", complaint);
        model.addAttribute("files", complaint.getFiles());
        return "complaint-details";
    }

    @PostMapping("/user/complaints")
    public String submitComplaint(
            @RequestParam("title") String title,
            @RequestParam("ward") Long ward,
            @RequestParam("description") String description,
//            @RequestParam("documentation") String documentation,
            @RequestParam("files") List<MultipartFile> files,
            @RequestParam("fileDescriptions") List<String> fileDescriptions,
            RedirectAttributes redirectAttributes, Principal principal) {
        String username = principal.getName(); // Get logged-in user
        AppUser user = userRepository.findByEmail(username).orElse(null);
        if(user==null){
            redirectAttributes.addFlashAttribute("error", "User Not Found or User is Deactivated");
            return "redirect:/user/complaint";
        }
        Ward ward1=wardRepository.findById(ward).orElse(null);

        // Create new complaint
        Complaint complaint = new Complaint();
        complaint.setTitle(title);
        complaint.setCreatedBy(user);
        if(ward1!=null){
        complaint.setWard(ward1);}
        complaint.setUuid(UUID.randomUUID());
        complaint.setDescription(description);

        // Save complaint first
        complaint = complaintRepository.save(complaint);
        System.out.println("Files list size: " + files.size());
        for (MultipartFile file : files) {
            System.out.println("File: " + file.getOriginalFilename() + ", isEmpty: " + file.isEmpty());
        }
//        List<String> savedFiles = new ArrayList<>();
        System.out.println(files.size()+"...................................................");
        if (!files.isEmpty()) {  // ✅ Correct check
            for (MultipartFile file : files) {
                if (file.isEmpty()) {
                    continue; // Skip empty file inputs
                }

                System.out.println("Processing file: " + file.getOriginalFilename());
                String file_description = (fileDescriptions.size() > files.indexOf(file)) ? fileDescriptions.get(files.indexOf(file)) : "No description";

                String savedFileName = fileStorageService.storeFile(file);
                String filePath = "uploads/" + savedFileName;

                UUID uuid=UUID.randomUUID();

                ComplaintFile complaintFile = new ComplaintFile(savedFileName, file_description, filePath, complaint,uuid);
                complaintFileRepository.save(complaintFile);
            }
        }
        // TODO: Save complaint details + savedFiles in the database.

        redirectAttributes.addFlashAttribute("success", "Complaint submitted successfully!");
        return "redirect:/user/complaint";
    }

    @PostMapping("/anonymous/complaints")
    public String submitAnonymousComplaint(
            @RequestParam("title") String title,
            @RequestParam("ward") Long ward,
            @RequestParam("description") String description,
//            @RequestParam("documentation") String documentation,
            @RequestParam("files") List<MultipartFile> files,
            @RequestParam("fileDescriptions") List<String> fileDescriptions,
            RedirectAttributes redirectAttributes) {
        Ward ward1=wardRepository.findById(ward).orElse(null);

        // Create new complaint
        Complaint complaint = new Complaint();
        complaint.setTitle(title);
        if(ward1!=null){
        complaint.setWard(ward1);}
        complaint.setUuid(UUID.randomUUID());
        complaint.setDescription(description);

        // Save complaint first
        complaint = complaintRepository.save(complaint);
        System.out.println("Files list size: " + files.size());
        for (MultipartFile file : files) {
            System.out.println("File: " + file.getOriginalFilename() + ", isEmpty: " + file.isEmpty());
        }
//        List<String> savedFiles = new ArrayList<>();
        System.out.println(files.size()+"...................................................");
        if (!files.isEmpty()) {  // ✅ Correct check
            for (MultipartFile file : files) {
                if (file.isEmpty()) {
                    continue; // Skip empty file inputs
                }

                System.out.println("Processing file: " + file.getOriginalFilename());
                String file_description = (fileDescriptions.size() > files.indexOf(file)) ? fileDescriptions.get(files.indexOf(file)) : "No description";

                String savedFileName = fileStorageService.storeFile(file);
                String filePath = "uploads/" + savedFileName;
                UUID uuid=UUID.randomUUID();

                ComplaintFile complaintFile = new ComplaintFile(savedFileName, file_description, filePath, complaint,uuid);
                complaintFileRepository.save(complaintFile);
            }
        }
        // TODO: Save complaint details + savedFiles in the database.

        redirectAttributes.addFlashAttribute("success", "Complaint submitted successfully!");
        return "redirect:/anonymous/complaint";
    }

    @GetMapping("/anonymous")
    public String anonymous() {
        return "redirect:/anonymous/complaint";
    }


    @GetMapping("/anonymous/complaint")
    public String anonymousComplaint(Model model) {
        List<SubCounty> subCounties = subCountyService.getAllSubCounties();
        model.addAttribute("subCounties", subCounties);
        model.addAttribute("complaint", new Complaint());
        return "anonymous-complaint-form";
    }


    @Autowired
    private SubCountyService subCountyService;
    @GetMapping("/user/complaint")
    public String showComplaintForm(Model model) {

        List<SubCounty> subCounties = subCountyService.getAllSubCounties();
        model.addAttribute("subCounties", subCounties);
        model.addAttribute("complaint", new Complaint());
        return "complaintForm";
    }





    @GetMapping("/complaints")
    public String listComplaints(Model model) {
        model.addAttribute("complaints", complaintRepository.findAllByOrderByCreatedAtDesc());
        return "complaintsList";
    }

    @GetMapping("/subcategories/{categoryId}")
    @ResponseBody
    public List<Subcategory> getSubcategories(@PathVariable Long categoryId) {
        return subcategoryRepository.findByCategoryId(categoryId);
    }
    @Autowired
    private ComplaintService complaintService;

    @PostMapping("/complaints/{complaintId}/notes")
    public String addInternalNote(@PathVariable Long complaintId, @RequestParam String note) {
        complaintService.addInternalNote(complaintId, note);
        return "redirect:/complaints/" + complaintId;
    }

//    @GetMapping("/complaints/{complaintId}")
//    public String showComplaintDetails(@PathVariable Long complaintId, Model model) {
//        Complaint complaint = complaintRepository.findById(complaintId).orElseThrow(() -> new RuntimeException("Complaint not found"));
//        model.addAttribute("complaint", complaint);
//        return "complaintDetails";
//    }

    @PostMapping("/complaints/{id}/assign")
    public String assignComplaintToUser(@PathVariable Long id,
                                        @RequestParam Long userId,
                                        RedirectAttributes redirectAttributes) {
        Optional<Complaint> optionalComplaint = complaintRepository.findById(id);
        if (optionalComplaint.isEmpty()) {
            redirectAttributes.addFlashAttribute("error", "Complaint not found.");
            return "redirect:/complaints";
        }

        Optional<AppUser> optionalUser = userRepository.findById(userId);
        if (optionalUser.isEmpty()) {
            redirectAttributes.addFlashAttribute("error", "User not found.");
            return "redirect:/complaints/" + id;
        }

        Complaint complaint = optionalComplaint.get();
        AppUser user = optionalUser.get();
        complaint.setAssignedTo(user);

        complaintRepository.save(complaint);

        redirectAttributes.addFlashAttribute("success", "Complaint successfully assigned to " + user.getUsername());
        return "redirect:/complaints/" + id;
    }

    @PostMapping("/complaints/{id}/resolve")
    public String resolveComplaint(
            @PathVariable Long id,
            @RequestParam("resolution") String resolution,
            RedirectAttributes redirectAttributes) {
        Optional<Complaint> optionalComplaint = complaintRepository.findById(id);

        if (optionalComplaint.isPresent()) {
            Complaint complaint = optionalComplaint.get();
            complaint.setResolution(resolution);
            complaint.setStatus(Status.RESOLVED);
            complaintRepository.save(complaint);

            redirectAttributes.addFlashAttribute("successMessage", "Complaint has been resolved successfully.");
        } else {
            redirectAttributes.addFlashAttribute("errorMessage", "Complaint not found.");
        }

        return "redirect:/complaints";
    }


}