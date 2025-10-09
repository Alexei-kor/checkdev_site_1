package ru.checkdev.site.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import ru.checkdev.site.dto.ProfileWithApprovedInterviewsDTO;
import ru.checkdev.site.dto.UsersApprovedInterviewsDTO;
import ru.checkdev.site.service.AuthService;
import ru.checkdev.site.service.NotificationService;
import ru.checkdev.site.service.ProfilesService;
import ru.checkdev.site.service.WisherService;
import ru.checkdev.site.util.RequestResponseTools;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

import static ru.checkdev.site.util.RequestResponseTools.getToken;

/**
 * CheckDev пробное собеседование
 * ProfilesController класс обработки запросов профилей.
 *
 * @author Dmitry Stepanov
 * @version 23.04.2023T10:31
 */
@Controller
@RequestMapping("/profiles")
@RequiredArgsConstructor
@Slf4j
public class ProfilesController {
    private final ProfilesService profilesService;
    private final AuthService authService;
    private final NotificationService notifications;
    private final WisherService wisherService;

    /**
     * Отображение вида одного ProfileDTO
     *
     * @param id      ID Profile
     * @param model   Model
     * @param request HttpServletRequest
     * @return String "oneProfile" page
     */
    @GetMapping("/{id}")
    public String getProfileById(@PathVariable int id, Model model, HttpServletRequest request) throws JsonProcessingException {
        String username = "";
        var profileOptional = profilesService.getProfileById(id);
        if (profileOptional.isPresent()) {
            model.addAttribute("profile", profileOptional.get());
            username = profileOptional.get().getUsername();
        }
        var token = getToken(request);
        if (token != null) {
            var userInfo = authService.userInfo(token);
            model.addAttribute("innerMessages", notifications.findBotMessageByUserId(token, userInfo.getId()));
        }
        long approvedInterviews = wisherService.getUserIdWithCountedApprovedInterviews(token, String.valueOf(id)).getApprovedInterviews();
        model.addAttribute("approvedInterviews", approvedInterviews);
        RequestResponseTools.addAttrBreadcrumbs(model,
                "Главная", "/",
                "Профили", "/profiles/",
                username, "/profiles/" + id
        );
        return "profiles/profileView";
    }

    /**
     * Отображение вида всех профилей List<ProfileDTO>
     *
     * @param model Model
     * @return String "/profiles" pge
     */
    @GetMapping("/")
    public String getAllProfiles(Model model, HttpServletRequest request) throws JsonProcessingException {
        RequestResponseTools.addAttrBreadcrumbs(model,
                "Главная", "/",
                "Профили", "/profiles/"
        );
        var token = getToken(request);
        List<UsersApprovedInterviewsDTO> usersApprovedInterviewsList =
                wisherService.getUsersIdWithCountedApprovedInterviews(token);
        List<ProfileWithApprovedInterviewsDTO> profileWithApprovedInterviewsDTO =
                profilesService.getAllProfilesWithApprovedInterviews(usersApprovedInterviewsList);
        model.addAttribute("profiles", profileWithApprovedInterviewsDTO);
        model.addAttribute("current_page", "profiles");
        if (token != null) {
            var userInfo = authService.userInfo(token);
            model.addAttribute("innerMessages", notifications.findBotMessageByUserId(
                    token, userInfo.getId()));
        }
        return "profiles/profiles";
    }
}
