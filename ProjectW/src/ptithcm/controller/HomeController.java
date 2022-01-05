package ptithcm.controller;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map.Entry;

import javax.servlet.http.HttpSession;
import javax.transaction.Transactional;
//import javax.validation.Valid;
import javax.validation.Valid;

import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.ui.ModelMap;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
//import org.springframework.validation.BindingResult;
//import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import ptithcm.entity.Role;
import ptithcm.entity.RegisterRequest;
import ptithcm.entity.ChangePasswordRequest;
import ptithcm.entity.LoginRequest;
import ptithcm.bean.Mailer;
import ptithcm.entity.Bill;
import ptithcm.entity.BillDetail;
import ptithcm.entity.BillStatus;
import ptithcm.entity.User;
import ptithcm.entity.Cart;
import ptithcm.entity.Product;
import ptithcm.entity.Category;
import ptithcm.entity.Contact;

@Controller
@Transactional
@RequestMapping("/")
public class HomeController {
	@Autowired
	SessionFactory factory;
	
	@Autowired
	HttpSession session;
	
	@Autowired
	Mailer mailer;
	
	public List<Category> getAllCategory() {
		Session session = factory.getCurrentSession();
		String hql = "FROM Category";
		Query query = session.createQuery(hql);
		List<Category> result = query.list();
		return result;
	}
	
	public List<Product> getNewProduct() {
		Session session = factory.getCurrentSession();
		String hql = "FROM Product r ORDER BY r.id DESC";
		Query query = session.createQuery(hql);
		query.setMaxResults(6);
		List<Product> result = query.list();
		return result;
	}
	
	public List<Product> getProductByCategory(int id) {
		Session session = factory.getCurrentSession();
		String hql = "FROM Product r WHERE r.category.id = :id";
		Query query = session.createQuery(hql);
		query.setParameter("id", id);
		List<Product> result = query.list();
		return result;
	}
	
	//----- Trang chủ -----
	@RequestMapping("index")
	public String index(ModelMap model) {
		List<Category> listCategory = getAllCategory();
		model.addAttribute("listCategory", listCategory);

		List<Product> listNewProduct = getNewProduct();
		model.addAttribute("listNewProduct", listNewProduct);

		List<Product> listProductPro = getProductByCategory(1);
		model.addAttribute("listProductPro", listProductPro);

		List<Product> listProductSlim = getProductByCategory(2);
		model.addAttribute("listProductSlim", listProductSlim);
		return "index";
	}
	
	//----- Giới thiệu -----
	@RequestMapping("about")
	public String about(ModelMap model)
	{
		List<Category> listCategory = getAllCategory();
		model.addAttribute("listCategory", listCategory);
		return "about";
	}
	
	//----- Liên hệ -----
	@RequestMapping(value = "contact", method = RequestMethod.GET)
	public String contact(ModelMap model) {
		List<Category> listCategory = getAllCategory();
		model.addAttribute("listCategory", listCategory);
		model.addAttribute("contact", new Contact());
		return "contact";
	}

	@RequestMapping(value = "contact", method = RequestMethod.POST)
	public String contactinsert(ModelMap model, @ModelAttribute("contact") Contact contact, BindingResult result, 
			   @RequestParam("email") String from,
			   //@RequestParam("lgbphongphu2017@gmail.com") String to,
			   @RequestParam("title") String subject,
			   @RequestParam("content") String body)
	{
		if (contact.getName().trim().length() == 0)
		{
			result.rejectValue("name", "contact", "Vui lòng nhập họ tên!");
		}
		if (contact.getEmail().trim().length() == 0)
		{
			result.rejectValue("email", "contact", "Vui lòng nhập email!");
		}
		boolean kt = checkEmail(contact.getEmail().trim());
		if (kt == false)
		{
			result.rejectValue("email", "contact", "Email này không tồn tại!");
		}
		if (contact.getTitle().trim().length() == 0)
		{
			result.rejectValue("title", "contact", "Vui lòng nhập tiêu đề!");
		}
		if (contact.getContent().trim().length() == 0)
		{
			result.rejectValue("content", "contact", "Vui lòng nhập nội dung!");
		}
		if (result.hasErrors())
		{
			List<Category> listCategory = getAllCategory();
			model.addAttribute("listCategory", listCategory);
			model.addAttribute("message", "Liên hệ thất bại!");
			return "contact";
		}
		else
		{
			//String to = "lgbphongphu2017@gmail.com";
			String to = "ps4gamemachine@gmail.com";
			try
			{
				mailer.send(from, to, subject, body);
				model.addAttribute("message", "Liên hệ thành công!");
			}
			catch (Exception ex)
			{
				model.addAttribute("message", "Liên hệ thất bại!");
			}
			List<Category> listCategory = getAllCategory();
			model.addAttribute("listCategory", listCategory);
			//model.addAttribute("message", "Liên hệ thành công!");
			model.addAttribute("contact", new Contact());
			return "contact";
		}
	}
	
	public List<Product> getAllProduct(Integer offset, Integer maxResults) {
		Session session = factory.getCurrentSession();
		String hql = "FROM Product p";
		Query query = session.createQuery(hql);
		query.setFirstResult((offset != null) ? offset : 0);
		query.setMaxResults((maxResults != null) ? maxResults : 6);
		List<Product> result = query.list();
		return result;
	}
	
	public List<Product> getAllProductCategory(Integer offset, Integer maxResults, int id) {
		Session session = factory.getCurrentSession();
		String hql = "FROM Product p WHERE p.category.id = :id";
		Query query = session.createQuery(hql);
		query.setParameter("id", id);
		query.setFirstResult((offset != null) ? offset : 0);
		query.setMaxResults((maxResults != null) ? maxResults : 6);
		List<Product> result = query.list();
		return result;
	}
	
	public long countTotalRecords() {
		Session session = factory.openSession();
		String countQ = "Select count (p.id) from Product p";
		Query countQuery = session.createQuery(countQ);
		return (Long) countQuery.uniqueResult();
	}
	
	public long countTotalRecordsCategory(int id) {
		Session session = factory.openSession();
		String countQ = "Select count (p.id) from Product p where p.category.id = :id";
		Query countQuery = session.createQuery(countQ);
		countQuery.setParameter("id", id);
		return (Long) countQuery.uniqueResult();
	}
	
	//----- Sản phẩm -----
	@RequestMapping("product")
	public String listProductHandler(ModelMap model, Integer offset, Integer maxResults) {
		List<Category> listCategory = getAllCategory();
		model.addAttribute("listCategory", listCategory);
		List<Product> list = getAllProduct(offset, maxResults);
		model.addAttribute("listProduct", list);
		model.addAttribute("count", countTotalRecords());
		model.addAttribute("offset", offset);
		return "product";
	}
	
	//----- Loại sản phẩm -----
	@RequestMapping("kinds/{id}")
	public String listProductKinds(ModelMap model, @PathVariable("id") int id, Integer offset, Integer maxResults)
	{
		List<Category> listCategory = getAllCategory();
		model.addAttribute("listCategory", listCategory);
		List<Product> list = getAllProductCategory(offset, maxResults, id);
		model.addAttribute("listProduct", list);
		model.addAttribute("count", countTotalRecordsCategory(id));
		model.addAttribute("offset", offset);
		return "product";
	}
	
	//----- Báo trang lỗi -----
	@RequestMapping("error")
	public String error404(ModelMap model) {
		List<Category> listCategory = getAllCategory();
		model.addAttribute("listCategory", listCategory);
		return "404";
	}
	
	public Product getProductById(int id) {
		Session session = factory.getCurrentSession();
		String hql = "FROM Product where id = :id";
		Query query = session.createQuery(hql);
		query.setParameter("id", id);
		Product result = (Product) query.uniqueResult();
		return result;
	}
	
	//----- Chi tiết sản phẩm -----
	@RequestMapping(value = "/detail/{id}", method = RequestMethod.GET)
	public String getDetailProduct(Model model, @PathVariable("id") int id) {
		Product product = getProductById(id);
		if (product != null) {
			model.addAttribute("productDetail", product);
		}
		List<Category> listCategory = getAllCategory();
		model.addAttribute("listCategory", listCategory);
		return "detail";
	}
	
	public double totalPrice(HashMap<Integer, Cart> cartItems) {
		int money = 0;
		for (Entry<Integer, Cart> list : cartItems.entrySet()) {
			money += list.getValue().getProduct().getPrice() * list.getValue().getQuantity();
		}
		return money;
	}
	
	//----- Giỏ hàng -----
	@RequestMapping("shopping-cart")
	public String shoppingCart(ModelMap model) {
		List<Category> listCategory = getAllCategory();
		model.addAttribute("listCategory", listCategory);
		return "shopping-cart";
	}
	
	@RequestMapping(value = "shopping-cart/add/{productId}.html", method = RequestMethod.GET)
	public String addShoppingCart(ModelMap model, HttpSession session, @PathVariable("productId") int productId, @ModelAttribute("productDetail") Cart cart) {
		List<Category> listCategory = getAllCategory();
		model.addAttribute("listCategory", listCategory);

		int quantity = cart.getQuantity();
		System.out.println(quantity);
		
		HashMap<Integer, Cart> cartItems = (HashMap<Integer, Cart>) session.getAttribute("myCartItems");
		if (cartItems == null) {
			cartItems = new HashMap<>();
		}
		Product product = getProductById(productId);
		if (product != null) {
			if (cartItems.containsKey(productId)) {
				Cart item = cartItems.get(productId);
				item.setProduct(product);
				item.setQuantity(item.getQuantity() + 1);
				cartItems.put(productId, item);
			} else {
				Cart item = new Cart();
				item.setProduct(product);
				item.setQuantity(1);
				cartItems.put(productId, item);
			}
		}
		session.setAttribute("myCartItems", cartItems);
		session.setAttribute("myCartTotal", totalPrice(cartItems));
		session.setAttribute("myCartNum", cartItems.size());
		return "shopping-cart";
	}
	
	@RequestMapping(value = "shopping-cart/remove/{productId}.html", method = RequestMethod.GET)
	public String viewRemove(ModelMap mm, @PathVariable("productId") int productId) {
		HashMap<Integer, Cart> cartItems = (HashMap<Integer, Cart>) session.getAttribute("myCartItems");
		if (cartItems == null) {
			cartItems = new HashMap<>();
		}
		if (cartItems.containsKey(productId)) {
			cartItems.remove(productId);
		}
		session.setAttribute("myCartItems", cartItems);
		session.setAttribute("myCartTotal", totalPrice(cartItems));
		session.setAttribute("myCartNum", cartItems.size());
		return "shopping-cart";
	}
	
	public boolean paying(HashMap<Integer, Cart> cartItem, User user, double total) {
		Session session = factory.openSession();
		Transaction t = session.beginTransaction();
		try {
			Bill bill = new Bill();
			BillDetail billDetail = new BillDetail();
			Date date = new Date(System.currentTimeMillis());
			
			String hql = "FROM BillStatus as bs WHERE bs.id = 2";
			Query query = session.createQuery(hql);
			BillStatus status = (BillStatus) query.uniqueResult();
			
			bill.setBillStatus(status);
			bill.setUser(user);
			bill.setCheckin(date);
			bill.setTotal(total);
			session.persist(bill);
			for (Entry<Integer, Cart> list : cartItem.entrySet()) {
				billDetail.setBill(bill);
				billDetail.setProducts(list.getValue().getProduct());
				billDetail.setQuantity(list.getValue().getQuantity());
				session.evict(billDetail);
				session.save(billDetail);
			}
			t.commit();
			return true;
		} catch (Exception e) {
			t.rollback();
			System.out.println("Loi" + e);
		} finally {
			session.close();
		}
		return false;
	}
	
	@RequestMapping("shopping-cart/paying")
	public String paying(ModelMap model) {
		HashMap<Integer, Cart> cartItems = (HashMap<Integer, Cart>) session.getAttribute("myCartItems");
		User user = (User) session.getAttribute("usersession");
		if(user!=null) {
			boolean check = paying(cartItems, user, totalPrice(cartItems));
			if (check == true)
			{
				cartItems = new HashMap<>();
				session.setAttribute("myCartItems", cartItems);
				session.setAttribute("myCartTotal", 0);
				session.setAttribute("myCartNum", 0);
				return "thank-you";
			}
			else
			{
				return "404";
			}
		}
		return "redirect:/sign-in.html";
	}
	
	//----- Đăng nhập -----
	public User checkLogin(String username, String password) {
		Session session = factory.getCurrentSession();
		String hql = "FROM User as u where u.username= :username and u.password= :password and u.active=1";
		Query query = session.createQuery(hql);
		query.setParameter("username", username);
		query.setParameter("password", password);
		User result = (User) query.uniqueResult();
//		System.out.println(username);
//		System.out.println(password);
		return result;
	}
	
	@RequestMapping(value = "sign-in", method = RequestMethod.GET)
	public String signIn(ModelMap model) {
		List<Category> listCategory = getAllCategory();
		model.addAttribute("listCategory", listCategory);
		model.addAttribute("loginRequest", new LoginRequest());
		return "sign-in";
	}

	@RequestMapping(value = "sign-in", method = RequestMethod.POST)
	public String signIn(ModelMap model, @ModelAttribute("loginRequest") LoginRequest loginRequest) {
		User user = checkLogin(loginRequest.getUsername(), loginRequest.getPassword());
		if (user == null) {
			model.addAttribute("message", "Vui lòng kiểm tra lại tài khoản và mật khẩu!");
			return "sign-in";
		}
//		if (user.getRole().getId() == 2)
//		{
//			model.addAttribute("loginRequest", user);
//			return "/admin/index";
//		}
		session.setAttribute("usersession", user);
		return "redirect:/index.html";
	}
	
	public boolean changePassword(User user) {
		Session session = factory.openSession();
		Transaction t = session.beginTransaction();
		try {
			session.saveOrUpdate(user);
			t.commit();
			return true;
		} catch (Exception e) {
			t.rollback();
			//System.out.println("Loi" + e);
		} finally {
			session.close();
		}
		return false;
	}
	
	//----- Thông tin người dùng -----
	@RequestMapping("account-info")
	public String acountInfo(ModelMap model) {
		List<Category> listCategory = getAllCategory();
		model.addAttribute("listCategory", listCategory);
		model.addAttribute("changePassword", new ChangePasswordRequest());
		return "account-info";
	}

	@RequestMapping(value = "account-info", method = RequestMethod.POST)
	public String acountInfo(ModelMap model,
			@ModelAttribute("changePassword") ChangePasswordRequest changePasswordRequest,
			BindingResult errors) {
		if (changePasswordRequest.getPassword().trim().length() == 0)
		{
			errors.rejectValue("password", "changePasswordRequest", "Vui lòng nhập mật khẩu cũ!");
		}
		if (changePasswordRequest.getNewPassword().trim().length() == 0)
		{
			errors.rejectValue("newPassword", "changePasswordRequest", "Vui lòng nhập mật khẩu mới!");
		}
		if (changePasswordRequest.getPassword().trim().length() == 0)
		{
			errors.rejectValue("confirmNewPassword", "changePasswordRequest", "Vui lòng xác nhận mật khẩu mới!");
		}
		if (errors.hasErrors()) {
			return "account-info";
		} else {
			User user = (User) session.getAttribute("usersession");
			if (!changePasswordRequest.getPassword().equals(user.getPassword())) {
				model.addAttribute("message", "Vui lòng kiểm tra lại mật khẩu cũ!");
				return "account-info";
			}
			if (!changePasswordRequest.getNewPassword().equals(changePasswordRequest.getConfirmNewPassword())) {
				model.addAttribute("message", "Mật khẩu mới không trùng khớp!");
				return "account-info";
			}
			user.setPassword(changePasswordRequest.getNewPassword());
			boolean check = changePassword(user);
			if (check == true)
			{
				model.addAttribute("message", "Thay đổi mật khẩu thành công!");
			}
			else
			{
				model.addAttribute("message", "Thay đổi mật khẩu thất bại!");
			}
			return "account-info";
		}
	}
	
	//----- Đăng xuất -----
	@RequestMapping(value = "logout", method = RequestMethod.GET)
	public String logout(ModelMap model) {
		session.invalidate();
		return "redirect:/index.html";
	}
	
	public boolean createUser(User user) {
		Session session = factory.openSession();
		Transaction t = session.beginTransaction();
		try {
			String hql = "FROM Role as r WHERE r.id = 2";
			Query query = session.createQuery(hql);
			Role role = (Role) query.uniqueResult();
			user.setRole(role);
			//role.setUser(user);
			session.save(user);
			t.commit();
			return true;
		} catch (Exception e) {
			t.rollback();
			System.out.println("Loi" + e);
		} finally {
			session.close();
		}
		return false;
	}
	
	public boolean isExitUsername(String username) {
		Session session = factory.openSession();
		Transaction t = session.beginTransaction();
		try {
			String hql = "SELECT count(u.id) FROM User u WHERE u.username=:username";
			Query query = session.createQuery(hql);
			query.setParameter("username", username);
			Long result = (Long) query.uniqueResult();
			t.commit();
			if(result > 0) {
				return true;
			}
		} catch (Exception e) {
			t.rollback();
		} finally {
			session.close();
		}
		return false;
	}
	
	//----- Đăng ký -----
	public boolean checkEmail(String email)
	{
		Session session = factory.getCurrentSession();
		//Transaction t = session.beginTransaction();
		String hql = "FROM User WHERE email =:mail";
		Query query = session.createQuery(hql);
		query.setParameter("mail", email);
		User result = (User) query.uniqueResult();
		if (result != null)
		{
			return true;
		}
		return false;
	}
	
	@RequestMapping("sign-up")
	public String signUp(ModelMap model) {
		List<Category> listCategory = getAllCategory();
		model.addAttribute("listCategory", listCategory);
		model.addAttribute("registerRequest", new RegisterRequest());
		return "sign-up";
	}

	@RequestMapping(value = "sign-up", method = RequestMethod.POST)
	public String signUp(@ModelAttribute("registerRequest") RegisterRequest registerRequest, BindingResult errors)
	{
		if (registerRequest.getDisplayName().trim().length() == 0)
		{
			errors.rejectValue("displayName", "registerRequest", "Vui lòng nhập họ tên!");
		}
		if (registerRequest.getEmail().trim().length() == 0)
		{
			errors.rejectValue("email", "registerRequest", "Vui lòng nhập email!");
		}
		boolean kt = checkEmail(registerRequest.getEmail().trim());
		if (kt == true)
		{
			errors.rejectValue("email", "registerRequest", "Email này đã tồn tại!");
		}
		if (registerRequest.getPhone().trim().length() == 0)
		{
			errors.rejectValue("phone", "registerRequest", "Vui lòng nhập số điện thoại!");
		}
		if (registerRequest.getUsername().trim().length() == 0)
		{
			errors.rejectValue("username", "registerRequest", "Vui lòng nhập tài khoản!");
		}
		if (registerRequest.getPassword().trim().length() == 0)
		{
			errors.rejectValue("password", "registerRequest", "Vui lòng nhập mật khẩu!");
		}
		if (registerRequest.getConfirmPassword().trim().length() == 0)
		{
			errors.rejectValue("confirmPassword", "registerRequest", "Vui lòng xác nhận mật khẩu!");
		}
		if (isExitUsername(registerRequest.getUsername())) {
			errors.rejectValue("username", "registerRequest", "Tên tài khoản tồn tại");
		}
		if (!registerRequest.getConfirmPassword().equals(registerRequest.getPassword()))
		{
			errors.rejectValue("confirmPassword", "registerRequest", "Vui lòng xác nhận lại mật khẩu!");
		}
		if (errors.hasErrors()) {
			return "sign-up";
		} else {
			User user = new User();
			if (registerRequest.getPassword().equals(registerRequest.getConfirmPassword())) {
				BeanUtils.copyProperties(registerRequest, user);
				createUser(user);
				return "redirect:success.html";
			}
			return "sign-up";
		}

	}
	
	//----- Thông báo -----
	@RequestMapping("success")
	public String success(ModelMap model) {
		List<Category> listCategory = getAllCategory();
		model.addAttribute("listCategory", listCategory);
		return "success";
	}
}
