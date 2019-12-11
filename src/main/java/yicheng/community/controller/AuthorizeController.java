package yicheng.community.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import yicheng.community.dto.AccessTokenDTO;
import yicheng.community.dto.GithubUser;
import yicheng.community.mapper.UserMapper;
import yicheng.community.model.User;
import yicheng.community.provider.GithubProvider;

import javax.servlet.http.HttpServletRequest;
import java.util.UUID;

@Controller
public class AuthorizeController {
    @Autowired
    private GithubProvider githubProvider;

    @Autowired
    private UserMapper userMapper;

    @Value("${github.client.id}")
    private String clientId;
    @Value("${github.client.secret}")
    private String clientSecret;
    @Value("${github.redirect.uri}")
    private String redirectUri;

    @GetMapping("/callback")
    public String callback(@RequestParam(name = "code") String code,
                           @RequestParam(name = "state") String state,
                            HttpServletRequest request){
        System.out.println("testing");
        AccessTokenDTO accessTokenDTO = new AccessTokenDTO();
        accessTokenDTO.setCode(code);
        accessTokenDTO.setClient_id(clientId);
        accessTokenDTO.setClient_secret(clientSecret);
        accessTokenDTO.setRedirect_uri(redirectUri);
        accessTokenDTO.setState(state);
        System.out.println("testing2");
        String accessToken = githubProvider.getAccessToken(accessTokenDTO);
        GithubUser githubUser = githubProvider.getUser(accessToken);
        System.out.println("githubUser:"+githubUser.getLogin());
        if(githubUser != null)
        {
            User user = new User();
            user.setToken(UUID.randomUUID().toString());
            user.setName(githubUser.getLogin());
            user.setAccountId(String.valueOf(githubUser.getId()));
            user.setGmtCreate(System.currentTimeMillis());
            user.setGmtModified(user.getGmtCreate());
            userMapper.insert(user);
            System.out.println("testing3" + githubUser.getLogin());
            //登陆成功，写cookie和session
            request.getSession().setAttribute("githubUser",githubUser);
            return "redirect:/";
        }else{
            //登陆失败，重新登陆
            return "redirect:/";
        }
    }
}
