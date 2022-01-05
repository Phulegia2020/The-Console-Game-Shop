package ptithcm.controller;

import java.io.File;
import java.util.Date;
import java.util.List;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import javax.transaction.Transactional;

import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import ptithcm.entity.BillStatus;
import ptithcm.entity.Bill;
import ptithcm.entity.BillDetail;
import ptithcm.entity.Role;
import ptithcm.entity.Product;
import ptithcm.entity.Category;
import ptithcm.entity.LoginRequest;
import ptithcm.entity.User;

@Controller
@RequestMapping("/admin/")
@Transactional
public class AdminController {
	@Autowired
	SessionFactory factory;
	
	@Autowired
	HttpSession session;
	
	@Autowired
	ServletContext context;
	
	//----- Trang chủ -----
	@RequestMapping("index")
	public String index() {
		if(session.getAttribute("adminsession") != null) {
			return "admin/index";
		}
		return "404";
	}
	
	public User checkLoginAdmin(String username, String password) {
		Session session = factory.openSession();
		System.out.println(username);
		System.out.println(password);
		String hql = "FROM User u WHERE u.username = :username AND u.password = :password AND u.active = 1 AND u.role.id = 1";
		Query query = session.createQuery(hql);
		query.setParameter("username", username);
		query.setParameter("password", password);
		User result = (User) query.uniqueResult();
		return result;
	}
	
	//----- Đăng nhập -----
	@RequestMapping(value = "login", method = RequestMethod.GET)
	public String signInAdmin(ModelMap model) {
		model.addAttribute("loginRequest", new LoginRequest());
		return "admin/login";
	}

	@RequestMapping(value = "login", method = RequestMethod.POST)
	public String signInAdmin(ModelMap model, @ModelAttribute("loginRequest") LoginRequest loginRequest) {
		User user = checkLoginAdmin(loginRequest.getUsername(), loginRequest.getPassword());
		if (user == null) {
			model.addAttribute("message", "Vui lòng kiểm tra lại tài khoản và mật khẩu!");
			return "admin/login";
		}
		session.setAttribute("adminsession", user);
		return "admin/index";
	}
	
	//----- Đăng xuất -----
	@RequestMapping(value = "logout", method = RequestMethod.GET)
	public String logoutAdmin(ModelMap model) {
		session.invalidate();
		return "redirect:/admin/login.html";
	}
	
	//----- Quay về web -----
	@RequestMapping(value = "return", method = RequestMethod.GET)
	public String comeback(ModelMap model) {
		//session.invalidate();
		return "redirect:/index.html";
	}
	
	public List<Category> getAllCategory() {
		Session session = factory.getCurrentSession();
		String hql = "FROM Category";
		Query query = session.createQuery(hql);
		List<Category> result = query.list();
		return result;
	}
	
	public boolean insertCategory(Category category) {
		Session session = factory.openSession();
		Transaction t = session.beginTransaction();
		try {
			session.save(category);
			t.commit();
			return true;
		} catch (Exception e) {
			t.rollback();
		} finally {
			session.close();
		}
		return false;
	}
	
	public long totalCategory()
	{
		Session session = factory.getCurrentSession();
		String hql = "SELECT COUNT(id) FROM Category";
		Query query = session.createQuery(hql);
		return (Long)query.uniqueResult();
	}
	
	//----- Thể loại sản phẩm -----
	@RequestMapping("category")
	public String category(ModelMap model) {
		if (session.getAttribute("adminsession") != null) {
			List<Category> listCategory = getAllCategory();
			model.addAttribute("listCategory", listCategory);
			model.addAttribute("totalItem", totalCategory());
			model.addAttribute("countItem", totalCategory() % 10);
			return "admin/category/category_list";
		}
		return "404";
	}
	
	@RequestMapping(value = "insert", method = RequestMethod.GET)
	public String category_form(ModelMap model) {
		model.addAttribute("category", new Category());
		return "admin/category/category_insert";
	}

	@RequestMapping(value = "insert", method = RequestMethod.POST)
	public String category_form(ModelMap model, @ModelAttribute("category") Category category) {
		boolean check = insertCategory(category);
		if (check) {
			model.addAttribute("message", "Thêm mới thành công!");
		} else {
			model.addAttribute("message", "Thêm mới thất bại!");
		}
		return "admin/category/category_insert";
	}
	
	public boolean updateCategory(Category category) {
		Session session = factory.openSession();
		Transaction t = session.beginTransaction();
		try {
			session.update(category);
			t.commit();
			return true;
		} catch (Exception e) {
			t.rollback();
		} finally {
			session.close();
		}
		return false;
	}
	
	@RequestMapping(value = "category/update/{id}", method = RequestMethod.GET)
	public String edit(ModelMap model, @PathVariable("id") int id) {
		Session session = factory.getCurrentSession();
		Category category = (Category) session.get(Category.class, id);
		model.addAttribute("category", category);
		return "admin/category/category_update";
	}

	@RequestMapping(value = "category/update/{id}", method = RequestMethod.POST)
	public String edit(ModelMap model, @ModelAttribute("category") Category category) {
		boolean check = updateCategory(category);
		if (check) {
			model.addAttribute("message", "Cập nhật thành công!");
		} else {
			model.addAttribute("message", "Cập nhật thất bại!");
		}
		return "admin/category/category_update";
	}
	
	public boolean removeCategory(int id) {
		Session session = factory.openSession();
		Transaction t = session.beginTransaction();
		try {
			Category category = (Category) session.get(Category.class, id);
			session.delete(category);
			t.commit();
			return true;
		} catch (Exception e) {
			t.rollback();
		} finally {
			session.close();
		}
		return false;
	}
	
	@RequestMapping("category/remove/{id}")
	public String delete(ModelMap model, @PathVariable("id") int id) {
		boolean check = removeCategory(id);
		if (check) {
			model.addAttribute("message", "Xóa thành công!");
		} else {
			model.addAttribute("message", "Xóa thất bại!");
		}
		return "redirect:/admin/category.html";
	}
	
	public List<Product> getListNav(int start, int limit) {
		Session session = factory.openSession();
		Transaction transaction = null;
		try {
			transaction = session.beginTransaction();
			Query query = session.createQuery("FROM Product");
			query.setFirstResult(start);
			query.setMaxResults(limit);
			List<Product> list = query.list();
			transaction.commit();
			return list;
		} catch (Exception ex) {
			if (transaction != null) {
				transaction.rollback();
			}
			ex.printStackTrace();
		} finally {
			session.flush();
			session.close();
		}
		return null;
	}
	
	public long countTotalRecords()
	{
		Session session = factory.openSession();
		String countQ = "Select count (p.id) from Product p";
		Query countQuery = session.createQuery(countQ);
		return (Long) countQuery.uniqueResult();
	}
	
	//----- Sản phẩm -----
	@RequestMapping(value = "product", method = RequestMethod.GET)
	public String viewProductList(ModelMap model) {
		if (session.getAttribute("adminsession") != null) {
			model.addAttribute("listProduct", getListNav(0, 10));
			model.addAttribute("page", countTotalRecords() / 10);
			model.addAttribute("totalItem", countTotalRecords());
			model.addAttribute("countItem", countTotalRecords() % 10);
			return "admin/product/product_list";
		}
		return "404";
	}

	@RequestMapping(value = "product/{page}", method = RequestMethod.GET)
	public String viewProductListByPage(ModelMap model, @PathVariable("page") int page) {
		model.addAttribute("listProduct", getListNav((page - 1) * 10, 10));
		model.addAttribute("page", countTotalRecords() / 10);
		model.addAttribute("totalItem", countTotalRecords());
		if ((page * 10) > countTotalRecords()) 
		{
			model.addAttribute("countItem", countTotalRecords() - (page - 1) * 10);
		} 
		else 
		{
			model.addAttribute("countItem", 10);
		}
		return "admin/product/product_list";
	}
	
	public int checkCode(String code)
	{
		Session session = factory.getCurrentSession();
		String hql = "FROM Product WHERE code = :ma";
		Query query = session.createQuery(hql);
		query.setParameter("ma", code);
		Product p = (Product)query.uniqueResult();
		if (p != null)
		{
			return -1;
		}
		return 1;
	}
	
	public boolean insertProduct(Product product) {
		Session session = factory.openSession();
		Transaction t = session.beginTransaction();
		try {
			product.setUpdateAt(new Date());
			session.save(product);
			t.commit();
			return true;
		} catch (Exception e) {
			t.rollback();
		} finally {
			session.close();
		}
		return false;
	}
	
	@RequestMapping(value = "product/insert", method = RequestMethod.GET)
	public String product_form(ModelMap model) {
		model.addAttribute("product", new Product());
		model.addAttribute("categories", getAllCategory());
		return "admin/product/product_insert";
	}

	@RequestMapping(value = "product/insert", method = RequestMethod.POST)
	public String product_form(ModelMap model, @ModelAttribute("product") Product product,
			@RequestParam("photo") MultipartFile photo, HttpServletRequest request) {
		context = request.getSession().getServletContext();
		if (photo.isEmpty()) {
			model.addAttribute("message", "Vui lòng cho hình ảnh vào!");
			model.addAttribute("categories", getAllCategory());
			return "admin/product/product_insert";
		}
		else 
		{
			try 
			{
				int kt = checkCode(product.getCode());
				if (kt == 1)
				{
					photo.transferTo(
							new File("D:\\LapTrinhWeb\\WorkSpace\\ProjectW\\WebContent\\images\\" + photo.getOriginalFilename()));
					
					product.setUrl("images/" + photo.getOriginalFilename());
					
					boolean check = insertProduct(product);
					if (check) 
					{
						model.addAttribute("message", "Thêm sản phẩm mới thành công!");
					} 
					else 
					{
						model.addAttribute("message", "Thêm sản phẩm mới thất bại!");
					}
				}
				else
				{
					model.addAttribute("message", "Mã sản phẩm này đã tồn tại!");
				}
				model.addAttribute("categories", getAllCategory());
				return "admin/product/product_insert";
			} 
			catch (Exception e) 
			{
				System.out.println(e);
				model.addAttribute("message", "Lưu hình ảnh thất bại!");
			}
		}
		return "404";
	}
	
	public boolean updateProduct(Product product) {
		Session session = factory.openSession();
		Transaction t = session.beginTransaction();
		try {
			product.setUpdateAt(new Date());
			session.update(product);
			t.commit();
			return true;
		} catch (Exception e) {
			t.rollback();
		} finally {
			session.close();
		}
		return false;
	}
	
	@RequestMapping(value = "product/update/{id}", method = RequestMethod.GET)
	public String editProduct(ModelMap model, @PathVariable("id") int id) {
		Session session = factory.openSession();
		Product product = (Product) session.get(Product.class, id);
		model.addAttribute("product", product);
		model.addAttribute("categories", getAllCategory());
		return "admin/product/product_update";
	}

	@RequestMapping(value = "product/update/{id}", method = RequestMethod.POST)
	public String editProduct(ModelMap model, @ModelAttribute("product") Product product,
			@RequestParam("photo") MultipartFile photo, HttpServletRequest request) {
		context = request.getSession().getServletContext();
		if (!photo.isEmpty()) {
			try {
				photo.transferTo(new File("D:\\LapTrinhWeb\\WorkSpace\\ProjectW\\WebContent\\images\\" + photo.getOriginalFilename()));
				product.setUrl("images/" + photo.getOriginalFilename());
				boolean check = updateProduct(product);
				if (check) {
					model.addAttribute("message", "Cập nhật thành công!");
				} else {
					model.addAttribute("message", "Cập nhật thất bại!");
				}
				return "admin/product/product_update";
			}catch (Exception e) {
				
			}
		}
		boolean check = updateProduct(product);
		if (check) {
			model.addAttribute("message", "Cập nhật thành công!");
		} else {
			model.addAttribute("message", "Cập nhật thất bại!");
		}
		return "admin/product/product_update";
	}
	
	public boolean deleteProduct(int id) {
		Session session = factory.openSession();
		Transaction t = session.beginTransaction();
		try {
			Product product = (Product) session.get(Product.class, id);
			session.delete(product);
			t.commit();
			return true;
		} catch (Exception e) {
			t.rollback();
		} finally {
			session.close();
		}
		return false;
	}
	
	@RequestMapping("product/remove/{id}")
	public String deleteProduct(ModelMap model, @PathVariable("id") int id) {
		boolean check = deleteProduct(id);
		if (check) {
			model.addAttribute("message", "Xóa thành công!");
		} else {
			model.addAttribute("message", "Xóa thất bại!");
		}
		return "redirect:/admin/product.html";
	}
	
	public List<User> getAllUser(int start, int limit) {
		Session session = factory.openSession();
		Transaction transaction = null;
		try {
			transaction = session.beginTransaction();
			Query query = session.createQuery("FROM User");
			query.setFirstResult(start);
			query.setMaxResults(limit);
			List<User> list = query.list();
			transaction.commit();
			return list;
		} catch (Exception ex) {
			if (transaction != null) {
				transaction.rollback();
			}
			ex.printStackTrace();
		} finally {
			session.flush();
			session.close();
		}
		return null;
	}

	public Long countRecord() {
		Session session = factory.openSession();
		String countQ = "Select count (u.id) from User u";
		Query countQuery = session.createQuery(countQ);
		return (Long) countQuery.uniqueResult();
	}
	
	public boolean insertUser(User user) {
		Session session = factory.openSession();
		Transaction t = session.beginTransaction();
		try {
			String hql = "FROM Role as r WHERE r.id= :id";
			Query query = session.createQuery(hql);
			query.setParameter("id", user.getRole().getId());
			Role role = (Role) query.uniqueResult();
			user.setRole(role);
			//role.setUser(user);
			session.save(user);
			t.commit();
			return true;
		} catch (Exception e) {
			t.rollback();
		} finally {
			session.close();
		}
		return false;
	}

	public boolean updateUser(User user) {
		Session session = factory.openSession();
		Transaction t = session.beginTransaction();
		try {
			String hql = "FROM Role as r WHERE r.id= :id";
			Query query = session.createQuery(hql);
			query.setParameter("id", user.getRole().getId());
			Role role = (Role) query.uniqueResult();
			user.setRole(role);
			//role.setUser(user);
			session.flush();
			session.clear();
			session.saveOrUpdate(user);
			t.commit();
			return true;
		} catch (Exception e) {
			t.rollback();
			System.out.println(e);
		} finally {
			session.close();
		}
		return false;
	}
	
	public boolean deleteUser(Long id) {
		Session session = factory.openSession();
		Transaction t = session.beginTransaction();
		try {
			User user = (User) session.get(User.class, id);
			user.setActive(false);
			session.update(user);
			t.commit();
			return true;
		} catch (Exception e) {
			t.rollback();
		} finally {
			session.close();
		}
		return false;
	}
	
	public List<Role> getAllRole() {
		Session session = factory.openSession();
		try {
			String hql = "FROM Role r ORDER BY r.id DESC";
			Query query = session.createQuery(hql);
			query.setMaxResults(3);
			List<Role> result = query.list();
			if (result != null) {
				return result;
			}
		} catch (Exception e) {
			System.out.println(e);
		}
		return null;
	}
	
	public List<Role> getRoleAdmin()
	{
		Session session = factory.getCurrentSession();
		String hql = "FROM Role WHERE id = 1";
		Query query = session.createQuery(hql);
		List<Role> result = query.list();
		return result;
	}
	
	//----- Người dùng -----
	@RequestMapping(value = "user", method = RequestMethod.GET)
	public String viewUserList(ModelMap model) 
	{
		if (session.getAttribute("adminsession") != null)
		{
			model.addAttribute("listUser", getAllUser(0, 10));
			model.addAttribute("page", countRecord() / 10);
			model.addAttribute("totalItem", countRecord());
			//model.addAttribute("countItem", 10);
			if (countRecord() < 10)
			{
				model.addAttribute("countItem", countRecord());
			}
			else
			{
				model.addAttribute("countItem", 10);
			}
			return "admin/user/user_list";
		}
		return "404";
	}

	@RequestMapping(value = "user/{page}", method = RequestMethod.GET)
	public String viewUserListByPage(ModelMap model, @PathVariable("page") int page) {
		model.addAttribute("listUser", getAllUser((page - 1) * 10, 10));
		model.addAttribute("page", countRecord() / 10);
		model.addAttribute("totalItem", countRecord());
		if ((page * 10) > countRecord()) 
		{
			model.addAttribute("countItem", countRecord() - (page - 1) * 10);
		} 
		else 
		{
			model.addAttribute("countItem", 10);
		}
		return "admin/user/user_list";
	}
	
	@RequestMapping(value = "user/insert", method = RequestMethod.GET)
	public String insertUser(ModelMap model) {
		model.addAttribute("user", new User());
		model.addAttribute("role", getAllRole());
		//model.addAttribute("role", getRoleAdmin());
		return "admin/user/user_insert";
	}

	@RequestMapping(value = "user/insert", method = RequestMethod.POST)
	public String insertUser(ModelMap model, @ModelAttribute("user") User user) {
		boolean check = insertUser(user);
		if (check) {
			model.addAttribute("message", "Thêm mới thành công!");
		} else {
			model.addAttribute("message", "Thêm mới thất bại!");
		}
		model.addAttribute("role", getAllRole());
		return "admin/user/user_insert";
	}

	@RequestMapping(value = "user/update/{id}", method = RequestMethod.GET)
	public String editUser(ModelMap model, @PathVariable("id") Long id) {
		Session session = factory.openSession();
		User user = (User) session.get(User.class, id);
		model.addAttribute("user", user);
		model.addAttribute("role", getAllRole());
		return "admin/user/user_update";
	}

	@RequestMapping(value = "user/update/{id}", method = RequestMethod.POST)
	public String editUser(ModelMap model, @ModelAttribute("user") User user) {
		boolean check = updateUser(user);
		if (check) {
			model.addAttribute("message", "Cập nhật thành công!");
		} else {
			model.addAttribute("message", "Cập nhật thất bại!");
		}
		model.addAttribute("role", getAllRole());
		return "admin/user/user_update";
	}

	@RequestMapping("user/remove/{id}")
	public String deleteUser(ModelMap model, @PathVariable("id") Long id) {
		boolean check = deleteUser(id);
		if (check) {
			model.addAttribute("message", "Xóa thành công!");
		} else {
			model.addAttribute("message", "Xóa thất bại!");
		}
		return "redirect:/admin/user.html";
	}
	
	public List<Bill> getAllBill(int start, int limit) {
		Session session = factory.openSession();
		Transaction transaction = null;
		try {
			transaction = session.beginTransaction();
			Query query = session.createQuery("FROM Bill b ORDER BY b.id DESC");
			query.setFirstResult(start);
			query.setMaxResults(limit);
			List<Bill> list = query.list();
			transaction.commit();
			return list;
		} catch (Exception ex) {
			if (transaction != null) {
				transaction.rollback();
			}
			ex.printStackTrace();
		} finally {
			session.flush();
			session.close();
		}
		return null;
	}
	
	public long countTotalRecordsBill() {
		Session session = factory.openSession();
		String countQ = "Select count (b.id) from Bill b";
		Query countQuery = session.createQuery(countQ);
		return (Long) countQuery.uniqueResult();
	}
	
	public List<BillStatus> getAllBillStatus() {
		Session session = factory.openSession();
		try {
			String hql = "FROM BillStatus";
			Query query = session.createQuery(hql);
			List<BillStatus> result = query.list();
			if (result != null) {
				return result;
			}
		} catch (Exception e) {
			// TODO: handle exception
		}
		return null;
	}
	
	public boolean updateBillStatus(int id) {
		Session session = factory.openSession();
		Transaction t = session.beginTransaction();
		try {
			Bill bill = (Bill) session.get(Bill.class, id);
			BillStatus status = new BillStatus();
			status.setId(1);
			/*List<BillStatus> listStatus = getAllBillStatus();
			for (BillStatus billStatus : listStatus) {
				if(billStatus.getId() == 1) {
					status = billStatus;
				}
			}*/
			Date date = new Date(System.currentTimeMillis());
			bill.setCheckout(date);
			bill.setBillStatus(status);
			session.update(bill);
			t.commit();
			return true;
		} catch (Exception e) {
			t.rollback();
		} finally {
			session.close();
		}
		return false;
	}
	
	public boolean removeBill(int id) {
		Session session = factory.openSession();
		Transaction t = session.beginTransaction();
		try {
			Bill bill = (Bill) session.get(Bill.class, id);
			BillStatus status = new BillStatus();
			status.setId(3);
			/*List<BillStatus> listStatus = getAllBillStatus();
			for (BillStatus billStatus : listStatus) {
				if(billStatus.getId() == 3) {
					status = billStatus;
				}
			}*/
			Date date = new Date(System.currentTimeMillis());
			bill.setCheckout(date);
			bill.setBillStatus(status);
			session.update(bill);
			t.commit();
			return true;
		} catch (Exception e) {
			t.rollback();
		} finally {
			session.close();
		}
		return false;
	}
	
	public List<BillDetail> getBillDetail(int idBill) {
		Session session = factory.openSession();
		Transaction transaction = null;
		try {
			transaction = session.beginTransaction();
			Query query = session.createQuery("FROM BillDetail bd WHERE bd.bill.id = :idBill");
			query.setParameter("idBill", idBill);
			List<BillDetail> list = query.list();
			transaction.commit();
			return list;
		} catch (Exception ex) {
			if (transaction != null) {
				transaction.rollback();
			}
			ex.printStackTrace();
		} finally {
			session.flush();
			session.close();
		}
		return null;
	}
	
	public Bill getOneBill(int idBill) {
		Session session = factory.openSession();
		Transaction transaction = null;
		try {
			transaction = session.beginTransaction();
			Query query = session.createQuery("FROM Bill b WHERE b.id = :idBill");
			query.setParameter("idBill", idBill);
			Bill bill = (Bill) query.uniqueResult();
			transaction.commit();
			return bill;
		} catch (Exception ex) {
			if (transaction != null) {
				transaction.rollback();
			}
			ex.printStackTrace();
		} finally {
			session.flush();
			session.close();
		}
		return null;
	}
	
	//----- Hóa đơn -----
	@RequestMapping(value = "bill", method = RequestMethod.GET)
	public String viewBillList(ModelMap model) {
		if (session.getAttribute("adminsession") != null) {
		model.addAttribute("listBill", getAllBill(0, 10));
		model.addAttribute("page", countTotalRecordsBill() / 10);
		model.addAttribute("totalItem", countTotalRecordsBill());
		//model.addAttribute("countItem", 10);
		if (countRecord() < 10)
		{
			model.addAttribute("countItem", countTotalRecordsBill());
		}
		else
		{
			model.addAttribute("countItem", 10);
		}
		return "admin/bill/bill_list";
		}
		return "404";
	}

	@RequestMapping(value = "bill/{page}", method = RequestMethod.GET)
	public String viewBillListByPage(ModelMap model, @PathVariable("page") int page) {
		model.addAttribute("listBill", getAllBill((page - 1) * 10, 10));
		model.addAttribute("page", countTotalRecordsBill() / 10);
		model.addAttribute("totalItem", countTotalRecordsBill());
		if ((page * 10) > countTotalRecordsBill()) 
		{
			model.addAttribute("countItem", countTotalRecordsBill() - (page - 1) * 10);
		} else 
		{
			model.addAttribute("countItem", 10);
		}
		return "admin/bill/bill_list";
	}

	@RequestMapping(value = "bill/detail/{id}"/*, method = RequestMethod.GET*/)
	public String editBill(ModelMap model, @PathVariable("id") int idBill) {
		List<BillDetail> list = getBillDetail(idBill);
		model.addAttribute("listBillDetail", list);
		model.addAttribute("bill", getOneBill(idBill));
		return "admin/bill/billdetail_form";
	}

	/*@RequestMapping(value = "bill/detail/{id}", method = RequestMethod.POST)
	public String editBill(ModelMap model, @ModelAttribute("billDetail") BillDetail billDetail) {
//		boolean check = billService.updateProduct(billDetail);
//		if(check) {
//			model.addAttribute("message","Cập nhật thành công!");
//		}else {
//			model.addAttribute("message","Cập nhật thất bại!");
//		}
		return "admin/billdetail_form";
	}*/

	@RequestMapping("bill/updateStatus/{id}")
	public String updateBillStatus(ModelMap model, @PathVariable("id") int id) {
		boolean check = updateBillStatus(id);
		if (check) {
			model.addAttribute("message", "Đã thanh toán thành công!");
		} else {
			model.addAttribute("message", "Đã thanh toán thất bại!");
		}
		return "redirect:/admin/bill.html";
	}

	@RequestMapping("bill/remove/{id}")
	public String deleteBillStatus(ModelMap model, @PathVariable("id") int id) {
		boolean check = removeBill(id);
		if (check) {
			model.addAttribute("message", "Hủy đơn hàng thành công!");
		} else {
			model.addAttribute("message", "Hủy đơn hàng thất bại!");
		}
		return "redirect:/admin/bill.html";
	}
}
