// console.log("hello");

// const viewContactModal = document.getElementById("view_contact_modal");

// // options with default values
// const options = {
//     placement: 'bottom-right',
//     backdrop: 'dynamic',
//     backdropClasses:
//         'bg-gray-900/50 dark:bg-gray-900/80 fixed inset-0 z-40',
//     closable: true,
//     onHide: () => {
//         console.log('modal is hidden');
//     },
//     onShow: () => {
//         console.log('modal is shown');
//     },
//     onToggle: () => {
//         console.log('modal has been toggled');
//     },
// };

// // instance options object
// const instanceOptions = {
//   id: 'view_contact_modal',
//   override: true
// };

// const contactModal = new Modal(viewContactModal,options,instanceOptions);

// function openContactModal(){
//     contactModal.show();
// }


document.addEventListener('DOMContentLoaded', () => {
    console.log("hello");
    const baseUrl = "http://localhost:8080"

    const viewContactModal = document.getElementById("view_contact_modal");

    const options = {
        placement: 'bottom-right',
        backdrop: 'dynamic',
        backdropClasses: 'bg-gray-900/50 dark:bg-gray-900/80 fixed inset-0 z-40',
        closable: true,
        onHide: () => {
            console.log('modal is hidden');
        },
        onShow: () => {
            console.log('modal is shown');
        },
        onToggle: () => {
            console.log('modal has been toggled');
        },
    };

    const instanceOptions = {
        id: 'view_contact_modal',
        override: true
    };

    const contactModal = new Modal(viewContactModal, options, instanceOptions);

    function openContactModal() {
        contactModal.show();
    }

    window.openContactModal = function () {
        contactModal.show();
    };

    window.closeContactModal = function () {
        contactModal.hide();
    }

    window.loadContactData = async function (id) {
        console.log(id);
        try {

            const contact = await (await (fetch(`${baseUrl}/api/contacts/${id}`))).json();
            console.log(contact);
            // document.querySelector("#contact_name").innerHTML=data.name;
            // document.querySelector("#contact_email").innerHTML=data.email;
            document.getElementById('contact_name').innerHTML = contact.name;
            document.getElementById('contact_email').innerHTML = contact.email;
            document.getElementById('contact_phoneNumber').innerHTML = contact.phoneNumber;
            document.getElementById('contact_address').innerHTML = contact.address;
            document.getElementById('contact_description').innerHTML = contact.description;
            document.getElementById('contact_picture').src = contact.picture;
            document.getElementById('contact_favourite').checked = contact.favourite;
            openContactModal();

        } catch (error) {
            console.log("Error : " + error);
        }
    };



    // window.loadContactData = async function (id) {
    //     console.log("Button clicked with contact ID:", id);

    //     try {
    //         const response = await fetch(`http://localhost:8080/api/contacts/${id}`);
    //         const contact = await response.json();

    //         // Check each element before setting its innerHTML
    //         const nameElement = document.getElementById('contact_name');
    //         const emailElement = document.getElementById('contact_email');
    //         const phoneNumberElement = document.getElementById('contact_phoneNumber');
    //         const addressElement = document.getElementById('contact_address');
    //         const descriptionElement = document.getElementById('contact_description');
    //         const pictureElement = document.getElementById('contact_picture');
    //         const favouriteElement = document.getElementById('contact_favourite');

    //         if (nameElement) nameElement.innerHTML = contact.name || "N/A";
    //         if (emailElement) emailElement.innerHTML = contact.email || "N/A";
    //         if (phoneNumberElement) phoneNumberElement.innerHTML = contact.phoneNumber || "N/A";
    //         if (addressElement) addressElement.innerHTML = contact.address || "N/A";
    //         if (descriptionElement) descriptionElement.innerHTML = contact.description || "N/A";
    //         if (pictureElement) pictureElement.src = contact.picture || "default.jpg";
    //         if (favouriteElement) favouriteElement.checked = contact.favourite || false;

    //         openContactModal();

    //     } catch (error) {
    //         console.log("Error:", error);
    //     }
    // };



    // Attach the openContactModal function to a button click
    //document.getElementById('openModalButton').onclick = openContactModal; // Ensure this button exists in your HTML
    //document.getElementById('closeModalButton').onclick = closeContactModal; // Ensure this button exists in your HTML
});

async function deleteContact(id) {

    Swal.fire({
        title: "Are you sure?",
        text: "You won't be able to revert this!",
        icon: "warning",
        showCancelButton: true,
        confirmButtonColor: "#3085d6",
        cancelButtonColor: "#d33",
        confirmButtonText: "Yes, delete it!"
      }).then((result) => {
        if (result.isConfirmed) {
          Swal.fire({
            title: "Deleted!",
            text: "Your file has been deleted.",
            icon: "success"
          });
          const url = `${baseUrl}/user/contacts/delete/`+id;
          window.location.replace(url);
        }
      });
    
}