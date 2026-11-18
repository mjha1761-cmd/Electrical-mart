package com.electricalmart.app;

import android.app.Activity;
import android.app.AlertDialog;
import android.os.Bundle;
import android.graphics.Color;
import android.graphics.Typeface;
import android.graphics.drawable.GradientDrawable;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;
import java.util.ArrayList;
import java.util.List;

/** A self-contained, offline-first demo of the Electrical Mart marketplace. */
public class MainActivity extends Activity {
    private static final int NAVY = Color.rgb(19, 39, 73);
    private static final int BLUE = Color.rgb(33, 99, 190);
    private static final int PALE = Color.rgb(244, 247, 251);
    private final List<Product> products = new ArrayList<>();
    private final List<String> cart = new ArrayList<>();
    private String role = "Customer";
    private String page = "Home";
    private LinearLayout content;

    private static class Product {
        String name, category;
        int price, stock;
        Product(String name, String category, int price, int stock) {
            this.name = name; this.category = category; this.price = price; this.stock = stock;
        }
    }

    @Override public void onCreate(Bundle state) {
        super.onCreate(state);
        products.add(new Product("Copper House Wire (90 m)", "Wires", 1850, 24));
        products.add(new Product("LED Bulb 9W — Pack of 4", "Lighting", 320, 40));
        products.add(new Product("Modular Switch 6A", "Switches", 125, 60));
        products.add(new Product("MCB 32A Double Pole", "Safety", 540, 18));
        showPage("Home");
    }

    private int dp(float value) { return (int) (value * getResources().getDisplayMetrics().density + 0.5f); }

    private GradientDrawable rounded(int color) {
        GradientDrawable bg = new GradientDrawable();
        bg.setColor(color); bg.setCornerRadius(dp(14));
        return bg;
    }

    private TextView text(String value, int size, int color, boolean bold) {
        TextView t = new TextView(this);
        t.setText(value); t.setTextSize(size); t.setTextColor(color);
        if (bold) t.setTypeface(Typeface.DEFAULT, Typeface.BOLD);
        return t;
    }

    private Button button(String label, Runnable action) {
        Button b = new Button(this);
        b.setText(label); b.setTextColor(Color.WHITE); b.setAllCaps(false);
        b.setBackground(rounded(BLUE));
        b.setOnClickListener(v -> action.run());
        return b;
    }

    private void gap(LinearLayout parent, int height) {
        View v = new View(this); v.setLayoutParams(new LinearLayout.LayoutParams(1, dp(height))); parent.addView(v);
    }

    private void showPage(String next) {
        page = next;
        LinearLayout root = new LinearLayout(this);
        root.setOrientation(LinearLayout.VERTICAL);
        root.setBackgroundColor(PALE);

        LinearLayout header = new LinearLayout(this);
        header.setGravity(Gravity.CENTER_VERTICAL);
        header.setPadding(dp(18), dp(12), dp(12), dp(12));
        header.setBackgroundColor(NAVY);
        TextView brand = text("⚡ Electrical Mart", 20, Color.WHITE, true);
        header.addView(brand, new LinearLayout.LayoutParams(0, dp(52), 1));
        Button account = new Button(this);
        account.setText(role + " ▾"); account.setTextColor(Color.WHITE); account.setAllCaps(false);
        account.setBackground(rounded(BLUE)); account.setOnClickListener(v -> chooseRole());
        header.addView(account);
        root.addView(header);

        ScrollView scroll = new ScrollView(this);
        content = new LinearLayout(this); content.setOrientation(LinearLayout.VERTICAL);
        content.setPadding(dp(18), dp(16), dp(18), dp(24));
        scroll.addView(content); root.addView(scroll, new LinearLayout.LayoutParams(-1, 0, 1));

        LinearLayout nav = new LinearLayout(this);
        nav.setGravity(Gravity.CENTER); nav.setPadding(dp(6), dp(5), dp(6), dp(5)); nav.setBackgroundColor(Color.WHITE);
        String[] tabs = {"Home", "Products", "Cart", "Services"};
        for (String tab : tabs) {
            Button b = new Button(this); b.setText(tab); b.setTextSize(12); b.setAllCaps(false);
            b.setTextColor(page.equals(tab) ? BLUE : NAVY); b.setBackgroundColor(Color.TRANSPARENT);
            b.setOnClickListener(v -> showPage(tab));
            nav.addView(b, new LinearLayout.LayoutParams(0, dp(52), 1));
        }
        root.addView(nav);
        setContentView(root);
        renderPage();
    }

    private void renderPage() {
        content.removeAllViews();
        if (page.equals("Cart")) { renderCart(); return; }
        if (page.equals("Services")) { renderServices(); return; }
        if (page.equals("Dashboard")) { renderDashboard(); return; }
        if (page.equals("Products") || role.equals("Customer")) renderCatalog();
        else renderDashboard();
    }

    private void renderCatalog() {
        content.addView(text(page.equals("Home") ? "Namaste! Welcome to your electrical store" : "Electrical products", 22, NAVY, true));
        gap(content, 6);
        content.addView(text("Quality products, trusted sellers, and local electrician services.", 14, Color.DKGRAY, false));
        gap(content, 14);
        EditText search = new EditText(this); search.setSingleLine(true); search.setHint("Search wire, lights, switches...");
        search.setPadding(dp(14), 0, dp(14), 0); search.setBackground(rounded(Color.WHITE));
        content.addView(search, new LinearLayout.LayoutParams(-1, dp(50)));
        gap(content, 14);
        content.addView(text("Popular products", 18, NAVY, true)); gap(content, 8);
        for (Product p : products) {
            LinearLayout card = card();
            card.addView(text(p.category.toUpperCase(), 11, BLUE, true)); gap(card, 4);
            card.addView(text(p.name, 16, NAVY, true));
            card.addView(text("₹" + p.price + "   •   In stock: " + p.stock, 14, Color.DKGRAY, false));
            gap(card, 5);
            card.addView(button("Add to cart", () -> {
                cart.add(p.name); Toast.makeText(this, p.name + " added to cart", Toast.LENGTH_SHORT).show();
            }));
            content.addView(card); gap(content, 10);
        }
    }

    private LinearLayout card() {
        LinearLayout c = new LinearLayout(this); c.setOrientation(LinearLayout.VERTICAL);
        c.setPadding(dp(16), dp(14), dp(16), dp(14)); c.setBackground(rounded(Color.WHITE));
        return c;
    }

    private void renderCart() {
        content.addView(text("Your cart", 23, NAVY, true)); gap(content, 12);
        if (cart.isEmpty()) {
            content.addView(text("Your cart is empty. Browse products and add items.", 15, Color.DKGRAY, false));
            gap(content, 12); content.addView(button("Browse products", () -> showPage("Products"))); return;
        }
        int total = 0;
        for (String item : cart) {
            Product p = findProduct(item); if (p != null) total += p.price;
            content.addView(text("•  " + item + (p == null ? "" : " — ₹" + p.price), 15, NAVY, false)); gap(content, 8);
        }
        content.addView(text("Total: ₹" + total, 19, NAVY, true)); gap(content, 14);
        content.addView(button("Place order", () -> new AlertDialog.Builder(this).setTitle("Order placed")
                .setMessage("Your demo order has been received. Sign-in and payment will be connected in the next stage.")
                .setPositiveButton("Done", (d, w) -> { cart.clear(); showPage("Home"); }).show()));
    }

    private Product findProduct(String name) {
        for (Product p : products) if (p.name.equals(name)) return p;
        return null;
    }

    private void renderServices() {
        content.addView(text("Electrician / Wireman services", 22, NAVY, true)); gap(content, 8);
        content.addView(text("Find local professionals for wiring, repairs, installation, and maintenance.", 15, Color.DKGRAY, false));
        gap(content, 14);
        LinearLayout c = card(); c.addView(text("Register as a wireman", 18, NAVY, true));
        c.addView(text("Create a profile and receive nearby service enquiries.", 14, Color.DKGRAY, false)); gap(c, 8);
        c.addView(button("Switch to Wireman", () -> { role = "Wireman"; showPage("Dashboard"); }));
        content.addView(c); gap(content, 12);
        content.addView(text("Sample service request", 18, NAVY, true)); gap(content, 6);
        content.addView(button("Request electrician", () -> new AlertDialog.Builder(this).setTitle("Request received")
                .setMessage("Your demo service enquiry is ready. Add login and location services to connect real professionals.")
                .setPositiveButton("OK", null).show()));
    }

    private void renderDashboard() {
        content.addView(text(role + " dashboard", 23, NAVY, true)); gap(content, 6);
        if (role.equals("Seller")) {
            content.addView(text("Manage your products, stock, and customer orders.", 14, Color.DKGRAY, false)); gap(content, 14);
            content.addView(button("Add a sample product", () -> addProductDialog())); gap(content, 14);
            content.addView(text("Your listings", 18, NAVY, true)); gap(content, 8);
            for (Product p : products) {
                LinearLayout c = card(); c.addView(text(p.name, 16, NAVY, true));
                c.addView(text("₹" + p.price + " • Stock " + p.stock, 14, Color.DKGRAY, false)); content.addView(c); gap(content, 8);
            }
        } else if (role.equals("Wireman")) {
            content.addView(text("Profile: Local electrical professional\nServices: Wiring • Repairs • Installation\nAvailability: Accepting enquiries", 15, Color.DKGRAY, false)); gap(content, 14);
            content.addView(button("Update availability", () -> Toast.makeText(this, "Availability updated (demo)", Toast.LENGTH_SHORT).show()));
            gap(content, 10); content.addView(text("New service enquiries will appear here when the app is connected to a backend.", 14, Color.DKGRAY, false));
        } else if (role.equals("Admin")) {
            content.addView(text("Platform overview (demo data)", 14, Color.DKGRAY, false)); gap(content, 12);
            content.addView(statCard("Users", "Customers, sellers, and wiremen")); gap(content, 8);
            content.addView(statCard("Catalog", products.size() + " product listings")); gap(content, 8);
            content.addView(statCard("Orders", cart.isEmpty() ? "No demo orders yet" : "" + cart.size() + " cart item(s)")); gap(content, 14);
            content.addView(button("Review product catalog", () -> showPage("Products")));
        } else {
            content.addView(text("Browse products, place orders, and request a wireman.", 15, Color.DKGRAY, false));
            gap(content, 12); content.addView(button("Browse products", () -> showPage("Products")));
        }
    }

    private LinearLayout statCard(String title, String value) {
        LinearLayout c = card(); c.addView(text(title, 17, NAVY, true)); c.addView(text(value, 14, Color.DKGRAY, false)); return c;
    }

    private void addProductDialog() {
        LinearLayout form = new LinearLayout(this); form.setPadding(dp(20), dp(4), dp(20), 0); form.setOrientation(LinearLayout.VERTICAL);
        EditText name = new EditText(this); name.setHint("Product name"); form.addView(name);
        EditText price = new EditText(this); price.setHint("Price in ₹"); price.setInputType(2); form.addView(price);
        new AlertDialog.Builder(this).setTitle("Add product").setView(form)
            .setNegativeButton("Cancel", null).setPositiveButton("Add", (d, w) -> {
                String n = name.getText().toString().trim(); String raw = price.getText().toString().trim();
                if (n.isEmpty() || raw.isEmpty()) { Toast.makeText(this, "Enter product name and price", Toast.LENGTH_SHORT).show(); return; }
                try { products.add(new Product(n, "Other", Integer.parseInt(raw), 1)); renderDashboard(); }
                catch (NumberFormatException e) { Toast.makeText(this, "Enter a valid price", Toast.LENGTH_SHORT).show(); }
            }).show();
    }

    private void chooseRole() {
        String[] roles = {"Customer", "Seller", "Wireman", "Admin"};
        new AlertDialog.Builder(this).setTitle("Choose demo role")
            .setItems(roles, (dialog, which) -> { role = roles[which]; showPage("Dashboard"); }).show();
    }
}
