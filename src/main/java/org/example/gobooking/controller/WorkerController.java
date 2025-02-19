package org.example.gobooking.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.example.gobooking.dto.auth.Finance;
import org.example.gobooking.dto.work.CreateServiceRequest;
import org.example.gobooking.dto.work.EditServiceRequest;
import org.example.gobooking.dto.work.EditWorkGraphicRequest;
import org.example.gobooking.security.CurrentUser;
import org.example.gobooking.service.CardService;
import org.example.gobooking.service.WorkGraphicService;
import org.example.gobooking.service.WorkService;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@Controller
@RequestMapping("/worker")
@RequiredArgsConstructor
public class WorkerController {

    private final CardService cardService;

    private final WorkGraphicService workGraphicService;

    private final WorkService workService;


    @GetMapping
    public String worker(@AuthenticationPrincipal CurrentUser user, ModelMap modelMap) {
        modelMap.addAttribute("cards", cardService.getCardsByUserId(user.getUser().getId()));
        Finance finance = new Finance();
        finance.setSpending(5000);
        modelMap.addAttribute("finance", finance);
        return "/worker/worker-panel";
    }

    @GetMapping("/my-work-graphic")
    public String createServicePage(@AuthenticationPrincipal CurrentUser user, ModelMap modelMap) {
        modelMap.put("workGraphic", workGraphicService.getWorkGraphicsByWorkerId(user.getUser().getId()));
        modelMap.put("weekdays", workGraphicService.weekDays());
        return "/worker/my-work-graphic";
    }

    @PostMapping("/edit-work-graphic")
    public String editWorkGraphic(@AuthenticationPrincipal CurrentUser user, @ModelAttribute EditWorkGraphicRequest editWorkGraphicRequest) {
        workGraphicService.editWorkGraphic(user.getUser().getId(), editWorkGraphicRequest);
        return "redirect:/worker/my-work-graphic";
    }

    @GetMapping("/my-services")
    public String myServices(@AuthenticationPrincipal CurrentUser user, ModelMap modelMap) {
        modelMap.put("services", workService.getServicesByWorkerId(user.getUser().getId()));
        return "/worker/my-services";
    }

    @GetMapping("/create-service")
    public String createService() {
        return "/worker/create-service";
    }

    @PostMapping("/create-service")
    public String createService(@AuthenticationPrincipal CurrentUser user,
                                @ModelAttribute @Valid CreateServiceRequest serviceRequest,
                                @RequestParam("image") MultipartFile image) {
        serviceRequest.setWorkerId(user.getUser().getId());
        workService.save(serviceRequest, image);
        return "redirect:/worker/my-services";
    }

    @GetMapping("/delete-service")
    public String deleteService(@RequestParam("serviceId") int serviceId) {
        workService.deleteById(serviceId);
        return "redirect:/worker/my-services";
    }

    @GetMapping("/edit-service")
    public String editServicePage(@RequestParam("serviceId") int serviceId,
                                  ModelMap modelMap) {
        modelMap.put("service", workService.getById(serviceId));
        return "/worker/edit-service";
    }

    @PostMapping("/edit-service")
    public String editService(@AuthenticationPrincipal CurrentUser user,
                              @ModelAttribute @Valid EditServiceRequest editServiceRequest,
                              @RequestParam("image") MultipartFile image) {
        editServiceRequest.setWorkerId(user.getUser().getId());
        workService.editService(editServiceRequest, image);
        return "redirect:/worker/my-services";
    }


}
