const services=[["Cardiology","Heart care for a healthier future.","❤","cardiology"],["Neurology","Expert care for your nervous system.","🧠","neurology"],["Orthopedics","Strong bones, active life.","🦴","orthopedics"],["Pediatrics","Healthy beginnings, bright futures.","👶","pediatrics"],["Dental Care","Healthy smiles, confident you.","🦷","dental"],["Diagnostics","Accurate results, better decisions.","🧪","diagnostics"]];
const doctors=[["Dr. Priya Sharma","Cardiologist","12+ Years Experience"],["Dr. Rahul Mehta","Neurologist","10+ Years Experience"],["Dr. Sneha Patil","Orthopedic Surgeon","8+ Years Experience"],["Dr. Amit Deshmukh","Pediatrician","7+ Years Experience"]];
const perks=[["Experienced Doctors","Skilled & certified professionals","👨‍⚕️"],["Advanced Technology","Latest medical equipment","🩻"],["24/7 Emergency","Always here for you","🚑"],["Patient Centered Care","Your health is our priority","🤝"]];
const facilities=[["Operation Theater","operation"],["ICU","icu"],["Patient Rooms","rooms"],["Laboratory","lab"],["Pharmacy","pharmacy"],["Reception","reception"]];
const reviews=[["Priya S.","Excellent service and very caring staff. The doctors are highly professional."],["Rahul K.","I had a great experience. The hospital is clean, well-organized and the staff is very supportive."],["Neha T.","Best hospital in the city. I felt safe and well taken care of throughout my treatment."]];
const $=id=>document.getElementById(id),initials=n=>n.replace(/Dr\. /,"").split(" ").map(w=>w[0]).join("");

$("serviceGrid").innerHTML=services.map(([n,d,i,im])=>`<article class="card svc"><img src="images/${im}.jpg" alt="${n}" loading="lazy"><div class="t"><span class="ico">${i}</span><b>${n}</b><small>${d}</small></div></article>`).join("");
$("doctorGrid").innerHTML=doctors.map(([n,s,e],i)=>`<article class="card doc"><img src="images/doctor-${i+1}.jpg" alt="${n}" loading="lazy"><div class="t"><b>${n}</b><span class="sp">${s}</span><small>${e}</small><a href="#book">View Profile →</a></div></article>`).join("");
$("perks").innerHTML=perks.map(([t,d,i])=>`<li><span class="ico">${i}</span><div><b>${t}</b><small>${d}</small></div></li>`).join("");
$("facilityGrid").innerHTML=facilities.map(([f,im])=>`<article class="card fac"><img src="images/${im}.jpg" alt="${f}" loading="lazy"><span>${f}</span></article>`).join("");
$("reviewGrid").innerHTML=reviews.map(([n,t],i)=>`<article class="card rev"><img src="images/patient-${i+1}.jpg" alt="${n}"><div><p>“${t}”</p><b>${n}</b><span class="stars">★★★★★</span></div></article>`).join("");

// booking form (talks to the Express backend)
const dep=$("deptSel"),doc=$("docSel");
const fill=(el,items)=>{el.length=1;items.forEach(([v,t])=>el.add(new Option(t,v)))};
async function api(url,opt){const r=await fetch((window.API_BASE||"")+url,opt);const d=await r.json().catch(()=>({}));if(!r.ok)throw new Error(d.error||"Server error");return d}
const opts=d=>[d.name,`${d.name} — ${d.specialty}`];
async function loadLists(){
  try{
    const [ds,dc]=await Promise.all([api("/api/departments"),api("/api/doctors")]);
    fill(dep,ds.map(d=>[d,d]));window.DOCS=dc;fill(doc,dc.map(opts));
  }catch{ // backend offline: fall back to the static lists above
    window.DOCS=doctors.map(([n,s])=>({name:n,specialty:s}));
    fill(dep,services.map(([n])=>[n,n]));fill(doc,window.DOCS.map(opts));
  }
}
dep.addEventListener("change",()=>{ // filter doctors by department
  fill(doc,(window.DOCS||[]).filter(d=>!dep.value||!d.department||d.department===dep.value).map(opts));
});
loadLists();
$("dateIn").min=new Date().toISOString().split("T")[0];
$("apptForm").addEventListener("submit",async e=>{
  e.preventDefault();const f=e.target,m=$("formMsg"),b=f.querySelector("button");
  if(!f.checkValidity()){m.className="msg err";m.textContent="Please fill all required fields with valid details.";f.reportValidity();return;}
  b.disabled=true;m.className="msg";m.textContent="Booking…";
  try{
    const r=await api("/api/appointments",{method:"POST",headers:{"Content-Type":"application/json"},
      body:JSON.stringify({name:f.name.value,phone:f.phone.value,department:f.dept.value,doctor:f.doctor.value,date:f.date.value})});
    m.textContent=`Thank you, ${r.name}! Booking ${r.reference} received for ${r.date}. We will call to confirm.`;f.reset();
  }catch(err){m.className="msg err";m.textContent=err.message}
  finally{b.disabled=false}
});

// mobile menu + active link
const menu=$("menu");$("burger").onclick=()=>menu.classList.toggle("open");
menu.addEventListener("click",()=>menu.classList.remove("open"));
const links=[...menu.querySelectorAll("a")];
["home","about","services","doctors","facilities","testimonials","contact"].forEach(id=>{
  new IntersectionObserver(es=>es.forEach(e=>e.isIntersecting&&links.forEach(a=>a.classList.toggle("on",a.hash==="#"+id))),{rootMargin:"-45% 0px -50% 0px"}).observe($(id));
});
